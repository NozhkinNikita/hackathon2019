package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.domain.Point;
import com.hton.entities.PointEntity;
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
@RequestMapping(value = WebMvcConfig.USER_POINT_PATH)
public class PointController {

    @Autowired
    private CommonDao<Point, PointEntity> pointDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return new ResponseEntity<>(pointDao.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeUserById(@PathVariable("id") String id) {
        pointDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        return new ResponseEntity<>(pointDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createUser(@RequestBody Point point) {
        pointDao.save(point);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@RequestBody Point point) {
        pointDao.update(point);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
