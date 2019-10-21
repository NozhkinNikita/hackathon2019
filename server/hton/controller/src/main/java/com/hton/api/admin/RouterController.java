package com.hton.api.admin;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Location;
import com.hton.domain.Router;
import com.hton.domain.UserLocation;
import com.hton.entities.LocationEntity;
import com.hton.entities.RouterEntity;
import com.hton.entities.UserLocationEntity;
import com.hton.service.LocationValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.ADMIN_ROUTER_PATH)
public class RouterController {

    @Autowired
    private CommonDao<Router, RouterEntity> routerDao;

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @Autowired
    private LocationValidatorService locationValidatorService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getRouterById(@PathVariable("id") String id) {
        return new ResponseEntity<>(routerDao.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeRouterById(@PathVariable("id") String id) {
        routerDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getRouters(@RequestParam(required = false) String filter) {
        String userId = credentialUtils.getUserInfo().getId();
        List<UserLocation> userLocations = userLocationDao.getByCondition(new SimpleCondition.Builder()
                .setSearchField("userId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(userId)
                .build());

        if (userLocations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Condition incomeCondition = FilterUtils.parseFilter(filter);

        List<Condition> conditions = userLocations.stream().map(ul -> new SimpleCondition.Builder()
                .setSearchValue("locationId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(ul.getLocation().getId())
                .build()).collect(Collectors.toList());

        conditions.add(incomeCondition);
        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(conditions)
                .setSkip(incomeCondition.getSkip())
                .setTake(incomeCondition.getTake())
                .setSortDirection(incomeCondition.getSortDirection())
                .setSortField(incomeCondition.getSortField())
                .setMaskFields(incomeCondition.getMaskFields())
                .build();
        return new ResponseEntity<>(routerDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> createRouter(@RequestBody Router router) {
        ResponseEntity<?> checkResponse = checkRouterRequest(router);
        if (checkResponse.getStatusCode() != HttpStatus.OK) {
            return checkResponse;
        }
        Optional<UserLocation> userLocation = (Optional<UserLocation>) checkResponse.getBody();

        if (Objects.requireNonNull(userLocation).isPresent()) {
            return new ResponseEntity<>(routerDao.save(router), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("user is not in this location", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> updateRouter(@RequestBody Router router) {
        ResponseEntity<?> checkResponse = checkRouterRequest(router);
        if (checkResponse.getStatusCode() != HttpStatus.OK) {
            return checkResponse;
        }
        Optional<UserLocation> userLocation = (Optional<UserLocation>) checkResponse.getBody();

        if (Objects.requireNonNull(userLocation).isPresent()) {
            routerDao.update(router);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("user is not in this location", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> checkRouterRequest(Router router) {
        if (router.getLocationId() == null) {
            return new ResponseEntity<>("location id is null", HttpStatus.BAD_REQUEST);
        }
        Location location = locationDao.getById(router.getLocationId());
        if (location == null) {
            return new ResponseEntity<>("location does not exists", HttpStatus.BAD_REQUEST);
        }
        String login = credentialUtils.getCredentialLogin();

        Optional<UserLocation> userLocation = locationValidatorService.validateLocation(login, router.getLocationId());
        return new ResponseEntity<>(userLocation, HttpStatus.OK);
    }

}
