package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.UserLocationConditionHelper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("locationId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(id)
                .build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);
        Location location = userLocations.stream().findFirst().map(UserLocation::getLocation).orElse(null);
        if (location == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<User> users = userLocations.stream()
                .map(UserLocation::getUser).collect(Collectors.toList());
        return new ResponseEntity<>(new LocationUsersResponse(location, users), HttpStatus.OK);
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

        request.getUsers().forEach(user -> {
            Condition condition = UserLocationConditionHelper
                    .getUserLocationCondition(user.getId(), request.getLocaiotn().getId(), Collections.singletonList("id"));
            userLocationDao.getByCondition(condition).forEach(ul -> userLocationDao.remove(ul.getId()));
            UserLocation userLocation = new UserLocation();
            userLocation.setUser(user);
            userLocation.setLocation(request.getLocation());
            userLocationDao.save(userLocation);

        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") String id) {
        locationDao.remove(id);
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
