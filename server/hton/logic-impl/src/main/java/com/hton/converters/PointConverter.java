package com.hton.converters;

import com.hton.domain.Point;
import com.hton.domain.RouterData;
import com.hton.entities.PointEntity;
import com.hton.entities.RouterDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PointConverter extends Converter<Point, PointEntity> {

    @Autowired
    private Converter<RouterData, RouterDataEntity> routerDataConverter;

    @Override
    public Class<Point> getDomainClass() {
        return Point.class;
    }

    @Override
    public Class<PointEntity> getEntityClass() {
        return PointEntity.class;
    }

    @Override
    public void toDomainObject(PointEntity entity, Point domain) {
        if (entity != null) {
            if (entity.getRouterDates() != null) {
                domain.setRouterDates(entity.getRouterDates().stream().map(r -> {
                    RouterData routerData = new RouterData();
                    routerDataConverter.toDomainObject(r, routerData);
                    return routerData;
                }).collect(Collectors.toList()));
            }
            super.toDomainObject(entity, domain);
        }
    }

    @Override
    public void toEntityObject(Point domain, PointEntity entity) {
        if (domain != null) {
            if (domain.getRouterDates() != null) {
                entity.setRouterDates(domain.getRouterDates().stream().map(r -> {
                    RouterDataEntity routerDataEntity = new RouterDataEntity();
                    routerDataConverter.toEntityObject(r, routerDataEntity);
                    return routerDataEntity;
                }).collect(Collectors.toList()));
            }
            super.toEntityObject(domain, entity);
        }
    }
}
