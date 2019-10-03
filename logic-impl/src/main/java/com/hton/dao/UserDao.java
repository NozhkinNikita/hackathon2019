package com.hton.dao;

import com.hton.converters.Converter;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDao extends CommonDao<User, UserEntity> {

    @Autowired
    private Converter<User, UserEntity> userConverter;

    @Override
    public Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    public User getByLogin(String login) {
        SimpleCondition condition = new SimpleCondition.Builder()
                .setSearchField("login")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(login)
                .build();
        return userConverter.toDomainObject(getByCondition(condition).stream().findFirst().orElse(null));
    }

    public List<User> getAll() {
        return getByCondition(new SimpleCondition.Builder().build()).stream().map(e -> userConverter.toDomainObject(e)).collect(Collectors.toList());
    }
}
