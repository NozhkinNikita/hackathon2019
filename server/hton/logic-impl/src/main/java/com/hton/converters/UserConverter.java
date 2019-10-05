package com.hton.converters;

import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserEntity> {

    @Override
    public User toDomainObject(UserEntity entity) {
        return ConvertUtils.createUserWithLocations(entity);
    }

    @Override
    public UserEntity toEntityObject(User domain) {
        return ConvertUtils.createUserEntityWithLocations(domain);
    }
}
