package com.hton.config;

import com.hton.dao.CommonDao;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("login")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(login)
                .build();
        User user = userDao.getByCondition(condition).stream().findFirst().orElse(null);
        if (user != null) {
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.name()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(
                    user.getLogin(),
                    user.getPwd(),
                    user.getEnabled(),
                    user.getEnabled(),
                    user.getEnabled(),
                    user.getEnabled(),
                    authorities);
        } else {
            throw new BadCredentialsException("Wrong user name");
        }
    }
}
