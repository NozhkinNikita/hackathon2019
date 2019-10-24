package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@RequestMapping(value = WebMvcConfig.USER_LOCATION_PATH)
public class UserLocationController {

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String filter) {
        String login = credentialUtils.getCredentialLogin();
        Condition condition = FilterUtils.getFilterWithLogin(filter, "users.login", login);
        condition.setMaskFields(Arrays.asList("id", "name"));

        return new ResponseEntity<>(locationDao.getByCondition(condition), HttpStatus.OK);
    }
}
