package com.hton.api;

import com.hton.dao.CommonDao;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = WebMvcConfig.USER_INFO_PATH)
public class UserInfoController {

    @Autowired
    private CredentialUtils credentialUtils;

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        String login = credentialUtils.getCredentialLogin();
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("login")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(login)
                .build();
        return new ResponseEntity<>(userDao.getByCondition(condition).stream().findFirst().orElse(null), HttpStatus.OK);
    }
}
