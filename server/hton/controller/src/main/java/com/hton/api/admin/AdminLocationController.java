package com.hton.api.admin;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@Qualifier("adminLocationController")
@RequestMapping(value = WebMvcConfig.ADMIN_LOCATION_PATH)
public class AdminLocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity getLocation(@PathVariable("id") String id) {
        String login = credentialUtils.getCredentialLogin();
        Condition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("id")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(id)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("users.login")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(login)
                                .build()
                )
                .build();

        return new ResponseEntity<>(locationDao.getByCondition(condition), HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String filter) {
        String login = credentialUtils.getCredentialLogin();
        Condition condition = FilterUtils.getFilterWithLogin(filter, "users.login", login);
        condition.setMaskFields(Arrays.asList("id", "name"));

        return new ResponseEntity<>(locationDao.getByCondition(condition), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        locationDao.update(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
