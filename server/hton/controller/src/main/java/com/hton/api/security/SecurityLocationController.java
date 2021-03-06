package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.LocationUpdateRequest;
import com.hton.api.requests.UserToLocationRequest;
import com.hton.api.responses.LocationUsersResponse;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Location;
import com.hton.domain.User;
import com.hton.domain.UserLocation;
import com.hton.entities.LocationEntity;
import com.hton.entities.UserEntity;
import com.hton.entities.UserLocationEntity;
import com.hton.service.LocationValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@Qualifier("securityLocationController")
@RequestMapping(value = WebMvcConfig.SECURITY_LOCATION_PATH)
public class SecurityLocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private LocationValidatorService locationValidatorService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        Location location = locationDao.getById(id);
        if (location == null) {
            return new ResponseEntity<>("Location with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        ComplexCondition condition = new ComplexCondition.Builder().setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("locationId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(id)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("actualRelation")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(true)
                                .build()
                ).build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);

        List<String> userIds = userLocations.stream()
                .map(ul -> ul.getUser().getId()).collect(Collectors.toList());
        return new ResponseEntity<>(new LocationUsersResponse(location, userIds), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        condition.setMaskFields(Arrays.asList("id", "name"));
        return new ResponseEntity<>(locationDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        return new ResponseEntity<>(locationDao.save(location), HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest request) {

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getLocation() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        locationDao.update(request.getLocation());

        Condition condition = new SimpleCondition.Builder()
                .setSearchField("locationId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(request.getLocation().getId())
                .build();
        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            request.getUserIds().forEach(userId -> {
                userLocationDao.getByCondition(condition).stream().filter(ul ->
                        !request.getUserIds().contains(ul.getUser().getId()))
                        .forEach(ul -> {
                            ul.setActualRelation(false);
                            userLocationDao.update(ul);
                        });
                User user = userDao.getById(userId);
                if (user != null) {
                    Optional<UserLocation> userLocationOpt = locationValidatorService.validateLocation(user.getLogin(),
                            request.getLocation().getId());
                    UserLocation userLocation;
                    if (userLocationOpt.isPresent()) {
                        userLocation = userLocationOpt.get();
                        userLocation.setActualRelation(true);
                        userLocationDao.update(userLocation);
                    } else {
                        userLocation = new UserLocation();
                        userLocation.setUser(user);
                        userLocation.setLocation(request.getLocation());
                        userLocation.setActualRelation(true);
                        userLocationDao.save(userLocation);
                    }
                }
            });
        } else {
            userLocationDao.getByCondition(condition).forEach(ul -> {
                ul.setActualRelation(false);
                userLocationDao.update(ul);
            });
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") String id) {
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("userId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(id)
                .setMaskFields(Arrays.asList("id"))
                .build();
        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);
        locationDao.remove(id, userLocations.stream().map(UserLocation::getId).collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/addUserToLocation")
    public ResponseEntity<?> addUserToLocation(@RequestBody UserToLocationRequest request) {
        ResponseEntity<?> validationResponse = validateUserLocationRequest(request, false);
        if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return validationResponse;
        }

        UserLocation userLocation = (UserLocation) validationResponse.getBody();
        return new ResponseEntity<>(userLocationDao.save(userLocation), HttpStatus.OK);
    }

    @DeleteMapping(value = "/removeUserFromLocation")
    public ResponseEntity<?> removeUserFromLocation(@RequestBody UserToLocationRequest request) {
        ResponseEntity<?> validationResponse = validateUserLocationRequest(request, true);
        if (validationResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return validationResponse;
        }


        UserLocation userLocation = (UserLocation) validationResponse.getBody();
        userLocationDao.remove(userLocation.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> validateUserLocationRequest(@RequestBody UserToLocationRequest request, Boolean isRemoveRq) {
        User user = userDao.getById(request.getUserId());
        if (user == null) {
            return new ResponseEntity<>(String.format("User with id=%s does not exists", request.getUserId()), HttpStatus.BAD_REQUEST);
        }
        Location location = locationDao.getById(request.getLocationId());
        if (location == null) {
            return new ResponseEntity<>(String.format("Location with id=%s does not exists", request.getLocationId()), HttpStatus.BAD_REQUEST);
        }

        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("userId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(request.getUserId())
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("locationId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(request.getLocationId())
                                .build()
                )
                .build();

        UserLocation userLocation = userLocationDao.getByCondition(condition).stream().findFirst().orElse(null);
        if (isRemoveRq) {
            if (userLocation == null) {
                return new ResponseEntity<>(String.format("User: %s is not in location: %s",
                        request.getUserId(), request.getLocationId()), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userLocation, HttpStatus.OK);
        } else {
            if (userLocation != null) {
                return new ResponseEntity<>(String.format("User: %s is already in location: %s",
                        request.getUserId(), request.getLocationId()), HttpStatus.BAD_REQUEST);
            }
            userLocation = new UserLocation();
            userLocation.setUser(user);
            userLocation.setLocation(location);
            return new ResponseEntity<>(userLocation, HttpStatus.OK);
        }
    }
}
