package com.hton.converters;

import com.hton.domain.Location;
import com.hton.domain.User;
import com.hton.domain.UserLocation;
import com.hton.entities.LocationEntity;
import com.hton.entities.UserEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLocationConverter extends Converter<UserLocation, UserLocationEntity> {

    @Autowired
    private Converter<User, UserEntity> userConverter;

    @Autowired
    private Converter<Location, LocationEntity> locationConverter;

    @Override
    public Class<UserLocation> getDomainClass() {
        return UserLocation.class;
    }

    @Override
    public Class<UserLocationEntity> getEntityClass() {
        return UserLocationEntity.class;
    }

    @Override
    public void toDomainObject(UserLocationEntity entity, UserLocation domain) {
        if (entity.getLocation() != null) {
            Location location = new Location();
            locationConverter.toDomainObject(entity.getLocation(), location);
            domain.setLocation(location);
        }
        if (entity.getUser() != null) {
            User user = new User();
            userConverter.toDomainObject(entity.getUser(), user);
            domain.setUser(user);
        }
        domain.setId(entity.getId());
        domain.setActualRelation(entity.getActualRelation());
    }

    @Override
    public void toEntityObject(UserLocation domain, UserLocationEntity entity) {
        if (domain.getLocation() != null) {
            LocationEntity locationEntity = new LocationEntity();
            locationConverter.toEntityObject(domain.getLocation(), locationEntity);
            entity.setLocation(locationEntity);
        }
        if (domain.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            userConverter.toEntityObject(domain.getUser(), userEntity);
            entity.setUser(userEntity);
        }
        entity.setId(domain.getId());
        entity.setActualRelation(domain.getActualRelation());
    }
}
