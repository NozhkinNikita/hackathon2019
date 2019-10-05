package com.hton.dao;

import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends CommonDao<User, UserEntity> {

    @Override
    public Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }
}
