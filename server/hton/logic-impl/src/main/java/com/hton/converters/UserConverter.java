package com.hton.converters;

import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter extends Converter<User, UserEntity> {

    @Autowired
    private RoleConverter roleConverter;

    @Override
    public Class<User> getDomainClass() {
        return User.class;
    }

    @Override
    public Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    public void toDomainObject(UserEntity entity, User user) {
        if (entity != null) {
            user.setRoles(entity.getRoles().stream().map(r -> roleConverter.toDomainObject(r))
                    .collect(Collectors.toList()));
            super.toDomainObject(entity, user);
        }
    }

    @Override
    public void toEntityObject(User domain, UserEntity entity) {
        if (domain != null) {
            entity.setRoles(domain.getRoles().stream().map(r -> roleConverter.toEntityObject(r))
                    .collect(Collectors.toList()));
            super.toEntityObject(domain, entity);
        }
    }
}
