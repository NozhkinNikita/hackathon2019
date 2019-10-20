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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.USER_POINT_PATH)
public class PointController {

    @Autowired
    private CommonDao<Point, PointEntity> pointDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getPointById(@PathVariable("id") String id) {
        return new ResponseEntity<>(pointDao.getById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removePointById(@PathVariable("id") String id) {
        pointDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getPoints(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        condition.setMaskFields(Arrays.asList("id", "name", "begin", "end", "isRepeat"));
        return new ResponseEntity<>(pointDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createPoint(@RequestBody Point point) {
        return new ResponseEntity<>(pointDao.save(point), HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updatePoint(@RequestBody Point point) {
        pointDao.update(point);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
