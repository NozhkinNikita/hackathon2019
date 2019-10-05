package com.hton.converters;

import com.hton.domain.Location;
import com.hton.domain.Router;
import com.hton.entities.LocationEntity;
import com.hton.entities.RouterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LocationConverter implements Converter<Location, LocationEntity> {

    @Autowired
    private Converter<Router, RouterEntity> routerConverter;

    @Override
    public Location toDomainObject(LocationEntity entity) {
        Location location = ConvertUtils.createLocationWithUsers(entity);
        location.setRouters(entity.getRouters().stream().map(r -> routerConverter.toDomainObject(r))
                .collect(Collectors.toList()));
        return location;
    }

    @Override
    public LocationEntity toEntityObject(Location domain) {
        LocationEntity entity = ConvertUtils.createLocationEntityWithUsers(domain);
        entity.setRouters(domain.getRouters().stream().map(r -> routerConverter.toEntityObject(r))
                .collect(Collectors.toList()));
        return entity;
    }
}
