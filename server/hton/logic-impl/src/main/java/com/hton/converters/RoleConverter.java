package com.hton.converters;

import com.hton.entities.Role;
import com.hton.entities.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    public Role toDomainObject(RoleEntity entity) {
        return Role.valueOf(entity.getId());
    }

    public RoleEntity toEntityObject(Role role) {
        return new RoleEntity(role);
    }
}
