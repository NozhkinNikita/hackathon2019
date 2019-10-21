package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.UserUpdateRequest;
import com.hton.api.responses.UserLocationsResponse;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Location;
import com.hton.domain.User;
import com.hton.domain.UserLocation;
import com.hton.entities.UserEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("userId")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(id)
                .setMaskFields(Arrays.asList(
                        "id",
                        "user.id", "user.fio", "user.login", "user.enabled", "user.roles.id",
                        "location.id", "location.name"
                ))
                .build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);
        User user = userLocations.stream().findFirst().map(UserLocation::getUser).orElse(null);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<Location> locations = userLocations.stream()
                .map(UserLocation::getLocation).collect(Collectors.toList());
        return new ResponseEntity<>(new UserLocationsResponse(user, locations), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeUserById(@PathVariable("id") String id) {
        userDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String filter) {
        return new ResponseEntity<>(userDao.getByCondition(FilterUtils.parseFilter(filter)), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userDao.save(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request) {
        User origUser = userDao.getById(request.getUser().getId());
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (origUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        request.getUser().setPwd(origUser.getPwd());
        userDao.update(request.getUser());
        request.getLocaiotns().forEach(l -> {
            ComplexCondition condition = new ComplexCondition.Builder()
                    .setOperation(Operation.AND)
                    .setConditions(
                            new SimpleCondition.Builder()
                                    .setSearchField("userId")
                                    .setSearchCondition(SearchCondition.EQUALS)
                                    .setSearchValue(request.getUser().getId())
                                    .build(),
                            new SimpleCondition.Builder()
                                    .setSearchField("locationId")
                                    .setSearchCondition(SearchCondition.EQUALS)
                                    .setSearchValue(l.getId())
                                    .build()
                    )
                    .setMaskFields(Arrays.asList("id"))
                    .build();
            userLocationDao.getByCondition(condition).forEach(ul -> userLocationDao.remove(ul.getId()));
            UserLocation userLocation = new UserLocation();
            userLocation.setUser(request.getUser());
            userLocation.setLocation(l);
            userLocationDao.save(userLocation);

        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
