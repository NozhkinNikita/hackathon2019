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
        Location location = new Location();
        locationConverter.toDomainObject(entity.getLocation(), location);
        domain.setLocation(location);
        User user = new User();
        userConverter.toDomainObject(entity.getUser(), user);
        domain.setUser(user);
        domain.setId(entity.getId());
    }

    @Override
    public void toEntityObject(UserLocation domain, UserLocationEntity entity) {
        LocationEntity locationEntity = new LocationEntity();
        locationConverter.toEntityObject(domain.getLocation(), locationEntity);
        entity.setLocation(locationEntity);
        UserEntity userEntity = new UserEntity();
        userConverter.toEntityObject(domain.getUser(), userEntity);
        entity.setUser(userEntity);
        entity.setId(entity.getId());
    }
}
