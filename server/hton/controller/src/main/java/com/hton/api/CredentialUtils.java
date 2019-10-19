package com.hton.api;

import com.hton.config.UserDetails;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.User;
import com.hton.entities.Role;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CredentialUtils {

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    public String getCredentialLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public List<Role> getCredentialRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = Collections.EMPTY_LIST;
        if (principal instanceof UserDetails) {
            roles = ((UserDetails)principal).getRoles();
        }
        return roles;
    }

    public User getUserInfo() {
        String login = getCredentialLogin();
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("login")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(login)
                .build();

        return userDao.getByCondition(condition).stream().findFirst().orElse(null);
    }
}
