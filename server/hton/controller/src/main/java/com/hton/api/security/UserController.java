package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.responses.UserLocationsResponse;
import com.hton.dao.CommonDao;
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
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userDao.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
