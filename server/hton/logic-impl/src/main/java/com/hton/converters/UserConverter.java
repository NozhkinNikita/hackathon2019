package com.hton.converters;

import com.hton.domain.User;
import com.hton.entities.RoleEntity;
import com.hton.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter implements Converter<User, UserEntity> {

    @Override
    public User toDomainObject(UserEntity entity) {
        User user = null;
        if (entity != null) {
            user = new User();
            user.setId(entity.getId());
            user.setFio(entity.getFio());
            user.setLogin(entity.getLogin());
            user.setPwd(entity.getPwd());
            user.setRoles(entity.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toList()));
        }
        return user;
    }

    @Override
    public UserEntity toEntityObject(User domain) {
        UserEntity entity = null;
        if (domain != null) {
            entity = new UserEntity();
            entity.setId(domain.getId());
            entity.setFio(domain.getFio());
            entity.setLogin(domain.getLogin());
            entity.setPwd(domain.getPwd());
            entity.setRoles(domain.getRoles().stream().map(RoleEntity::new).collect(Collectors.toList()));
        }
        return entity;
    }
}
