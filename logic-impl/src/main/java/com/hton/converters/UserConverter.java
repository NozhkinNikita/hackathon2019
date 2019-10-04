package com.hton.converters;

import com.hton.domain.User;
import com.hton.entities.UserEntity;
import org.springframework.stereotype.Component;

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
            user.setRoleEntities(entity.getRoleEntities());
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
            entity.setRoleEntities(domain.getRoleEntities());
        }
        return entity;
    }
}
