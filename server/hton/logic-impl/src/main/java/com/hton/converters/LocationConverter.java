package com.hton.converters;

import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter implements Converter<Location, LocationEntity> {
    @Override
    public Location toDomainObject(LocationEntity entity) {
        Location location = null;

        if (entity != null) {
            location = new Location();
            location.setId(entity.getId());
            location.setName(entity.getName());
        }

        return location;
    }

    @Override
    public LocationEntity toEntityObject(Location domain) {
        LocationEntity locationEntity = null;

        if (domain != null) {
            locationEntity = new LocationEntity();
            locationEntity.setId(domain.getId());
            locationEntity.setName(domain.getName());
        }

        return locationEntity;
    }
}
