package com.hton.api.security;

import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier("securityLocationController")
@RequestMapping(value = WebMvcConfig.SECURITY_LOCATION_PATH)
public class SecurityLocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        return new ResponseEntity<>(locationDao.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        return new ResponseEntity<>(locationDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        locationDao.save(location);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        locationDao.update(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") String id) {
        locationDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
