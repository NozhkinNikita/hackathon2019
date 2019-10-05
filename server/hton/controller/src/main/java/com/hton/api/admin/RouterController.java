package com.hton.api.admin;

import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Router;
import com.hton.entities.RouterEntity;
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

@Controller
@RequestMapping(value = WebMvcConfig.ADMIN_ROUTER_PATH)
public class RouterController {

    @Autowired
    private CommonDao<Router, RouterEntity> routerDao;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return new ResponseEntity<>(routerDao.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeUserById(@PathVariable("id") String id) {
        routerDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String filter) {
        SimpleCondition condition = new SimpleCondition.Builder().setSearchField("id")
                .setSearchCondition(SearchCondition.NOT_NULL).build();
        return new ResponseEntity<>(routerDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createUser(@RequestBody Router router) {
        routerDao.save(router);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@RequestBody Router router) {
        routerDao.update(router);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}