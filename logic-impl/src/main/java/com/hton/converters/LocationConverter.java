package com.hton.converters;

import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter implements Converter<Location, LocationEntity> {
    @Override
    public Location toDomainObject(LocationEntity entity) {
        return ConvertUtils.createLocationWithUsers(entity);
    }

    @Override
    public LocationEntity toEntityObject(Location domain) {
        return ConvertUtils.createLocationEntityWithUsers(domain);
    }
}
