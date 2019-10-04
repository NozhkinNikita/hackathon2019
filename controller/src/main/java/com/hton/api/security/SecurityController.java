package com.hton.api.security;

import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = WebMvcConfig.SECURITY_PATH)
public class SecurityController {

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<String> getUser() {
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }

    @GetMapping(value = "/admin/{id}", produces = "application/json")
    public ResponseEntity<?> getAdmin(@PathVariable("id") String id) {
        return new ResponseEntity<>(userDao.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/admin/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userDao.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
