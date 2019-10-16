package com.hton.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping(value = WebMvcConfig.SECURITY_USERS_PATH)
public class UserController {

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return new ResponseEntity<>(userDao.getById(id), HttpStatus.OK);
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
        userDao.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userDao.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
