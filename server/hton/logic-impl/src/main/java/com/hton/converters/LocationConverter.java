package com.hton.converters;

import com.hton.domain.Location;
import com.hton.domain.Router;
import com.hton.domain.User;
import com.hton.entities.LocationEntity;
import com.hton.entities.RouterEntity;
import com.hton.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LocationConverter extends Converter<Location, LocationEntity> {

    @Autowired
    private Converter<Router, RouterEntity> routerConverter;


    @Override
    public Class<Location> getDomainClass() {
        return Location.class;
    }

    @Override
    public Class<LocationEntity> getEntityClass() {
        return LocationEntity.class;
    }

    @Override
    public void toDomainObject(LocationEntity entity, Location domain) {
        if (entity != null) {
            domain.setRouters(entity.getRouters().stream().map(r -> {
                Router router = new Router();
                routerConverter.toDomainObject(r, router);
                return router;
            }).collect(Collectors.toList()));
            super.toDomainObject(entity, domain);
        }
    }

    @Override
    public void toEntityObject(Location domain, LocationEntity entity) {
        if (domain != null) {
            entity.setRouters(domain.getRouters().stream().map(r -> {
                RouterEntity router = new RouterEntity();
                routerConverter.toEntityObject(r, router);
                return router;
            }).collect(Collectors.toList()));
            super.toEntityObject(domain, entity);
        }
    }
}
