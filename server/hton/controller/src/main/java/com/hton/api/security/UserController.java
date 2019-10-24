package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.UserLocationConditionHelper;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.UserUpdateRequest;
import com.hton.api.responses.UserLocationsResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.SECURITY_USERS_PATH)
public class UserController {

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        User user = userDao.getById(id);
        if (user == null) {
            return new ResponseEntity<>("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
        }

        ComplexCondition condition = new ComplexCondition.Builder().setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("userId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(id)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("actualRelation")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(true)
                                .build()
                ).setMaskFields(Arrays.asList(
                        "id", "actualRelation",
                        "user.id", "user.fio", "user.login", "user.enabled", "user.roles.id",
                        "location.id"
                )).build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);

        List<String> locationIds = userLocations.stream()
                .map(ul -> ul.getLocation().getId()).collect(Collectors.toList());
        return new ResponseEntity<>(new UserLocationsResponse(user, locationIds), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeUserById(@PathVariable("id") String id) {
        User user = userDao.getById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setEnabled(false);
        userDao.update(user);
        userLocationDao.getByCondition(new SimpleCondition.Builder()
                .setSearchField("userId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(id)
                .build()).forEach(ul -> {
                    ul.setActualRelation(false);
                    userLocationDao.update(ul);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String filter) {
        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        FilterUtils.parseFilter(filter),
                        new SimpleCondition.Builder()
                                .setSearchField("enabled")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(true)
                                .build()
                )
                .build();
        return new ResponseEntity<>(userDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return new ResponseEntity<>("Roles are empty", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDao.save(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request) {
        User origUser = userDao.getById(request.getUser().getId());
        if (request.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (origUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Condition condition = new SimpleCondition.Builder()
                .setSearchField("userId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(request.getUser().getId())
                .build();

        request.getUser().setPwd(origUser.getPwd());
        userDao.update(request.getUser());
        if (!CollectionUtils.isEmpty(request.getLocationIds())) {
            request.getLocationIds().forEach(locationId -> {
                userLocationDao.getByCondition(condition).stream().filter(ul ->
                        !request.getLocationIds().contains(ul.getLocation().getId())).forEach(ul -> {
                    ul.setActualRelation(false);
                    userLocationDao.update(ul);
                });
                Location location = locationDao.getById(locationId);
                if (location != null) {
                    UserLocation userLocation = new UserLocation();
                    userLocation.setUser(request.getUser());
                    userLocation.setLocation(location);
                    userLocation.setActualRelation(true);
                    userLocationDao.save(userLocation);
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
}
