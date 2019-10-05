package com.hton.api.security;

import com.hton.api.WebMvcConfig;
import com.hton.converters.Converter;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Location;
import com.hton.domain.User;
import com.hton.entities.LocationEntity;
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

@Controller
@RequestMapping(value = WebMvcConfig.LOCATION_PATH)
public class LocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        return new ResponseEntity<>(locationDao.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getAll() {
        Condition condition = new SimpleCondition.Builder()
                .setSearchField("id")
                .setSearchCondition(SearchCondition.NOT_EQUALS)
                .setSearchValue("0")
                .build();
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
