package com.hton.dao;

import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationDao extends CommonDao<Location, LocationEntity> {
    @Override
    public Class<LocationEntity> getEntityClass() {
        return LocationEntity.class;
    }
}
