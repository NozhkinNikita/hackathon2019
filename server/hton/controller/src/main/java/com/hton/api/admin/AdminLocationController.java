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
import com.hton.domain.UserLocation;
import com.hton.entities.LocationEntity;
import com.hton.entities.UserLocationEntity;
import com.hton.service.LocationValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@Qualifier("adminLocationController")
@RequestMapping(value = WebMvcConfig.ADMIN_LOCATION_PATH)
public class AdminLocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private LocationValidatorService locationValidatorService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        String login = credentialUtils.getCredentialLogin();
        Condition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("locationId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(id)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("user.login")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(login)
                                .build()
                )
                .build();

        return new ResponseEntity<>(userLocationDao.getByCondition(condition).stream()
                .findFirst().orElse(null), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String filter) {
        String userId = credentialUtils.getUserInfo().getId();
        Condition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(new SimpleCondition.Builder()
                                .setSearchField("userId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(userId)
                                .build(),
                        FilterUtils.parseFilter(filter))
                .setMaskFields(Arrays.asList(
                        "id", "actualRelation",
                        "user.id", "user.fio", "user.login", "user.enabled", "user.roles.id",
                        "location.id", "location.name"
                ))
                .build();

        return new ResponseEntity<>(userLocationDao.getByCondition(condition).stream()
                .map(UserLocation::getLocation).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        locationDao.update(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
