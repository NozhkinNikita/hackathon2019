package com.hton.converters;

import com.hton.domain.Location;
import com.hton.domain.User;
import com.hton.entities.LocationEntity;
import com.hton.entities.RoleEntity;
import com.hton.entities.UserEntity;

import java.util.stream.Collectors;

public class ConvertUtils {

    public static User createUserWithoutLocations(UserEntity entity) {
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

    public static User createUserWithLocations(UserEntity entity) {
        User user = createUserWithoutLocations(entity);
        if (entity != null) {
            user.setLocations(entity.getLocations().stream().map(ConvertUtils::createLocationWithoutUsers)
                    .collect(Collectors.toList()));
        }
        return user;
    }

    public static UserEntity createUserEntityWithoutLocations(User domain) {
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

    public static UserEntity createUserEntityWithLocations(User domain) {
        UserEntity entity = createUserEntityWithoutLocations(domain);
        if (domain != null) {
            entity.setLocations(domain.getLocations().stream().map(ConvertUtils::createLocationEntityWithoutUsers)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    public static Location createLocationWithUsers(LocationEntity entity) {
        Location location = createLocationWithoutUsers(entity);
        if (entity != null) {
            location.setUsers(entity.getUsers().stream().map(ConvertUtils::createUserWithoutLocations)
                    .collect(Collectors.toList()));
        }
        return location;
    }

    public static Location createLocationWithoutUsers(LocationEntity entity) {
        Location location = null;

        if (entity != null) {
            location = new Location();
            location.setId(entity.getId());
            location.setName(entity.getName());
        }
        return location;
    }

    public static LocationEntity createLocationEntityWithoutUsers(Location domain) {
        LocationEntity locationEntity = null;

        if (domain != null) {
            locationEntity = new LocationEntity();
            locationEntity.setId(domain.getId());
            locationEntity.setName(domain.getName());
            locationEntity.setUsers(domain.getUsers().stream().map(ConvertUtils::createUserEntityWithoutLocations)
                    .collect(Collectors.toList()));
        }
        return locationEntity;
    }


    public static LocationEntity createLocationEntityWithUsers(Location domain) {
        LocationEntity locationEntity = createLocationEntityWithoutUsers(domain);

        if (domain != null) {
            locationEntity.setUsers(domain.getUsers().stream().map(ConvertUtils::createUserEntityWithoutLocations)
                    .collect(Collectors.toList()));
        }
        return locationEntity;
    }
}
