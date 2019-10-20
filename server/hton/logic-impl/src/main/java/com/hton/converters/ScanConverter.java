package com.hton.converters;

import com.hton.domain.Device;
import com.hton.domain.Point;
import com.hton.domain.Scan;
import com.hton.domain.UserLocation;
import com.hton.entities.DeviceEntity;
import com.hton.entities.PointEntity;
import com.hton.entities.ScanEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
public class ScanConverter extends Converter<Scan, ScanEntity> {

    @Autowired
    private Converter<Device, DeviceEntity> deviceConverter;

    @Autowired
    private Converter<Point, PointEntity> pointConverter;

    @Autowired
    private UserLocationConverter userLocationConverter;

    @Override
    public Class<Scan> getDomainClass() {
        return Scan.class;
    }

    @Override
    public Class<ScanEntity> getEntityClass() {
        return ScanEntity.class;
    }

    @Override
    public void toDomainObject(ScanEntity entity, Scan domain) {
        if (entity.getUserLocation() != null) {
            UserLocation userLocation = new UserLocation();
            userLocationConverter.toDomainObject(entity.getUserLocation(), userLocation);
            domain.setUserLocation(userLocation);
        }
        if (entity.getDevice() != null) {
            Device device = new Device();
            deviceConverter.toDomainObject(entity.getDevice(), device);
            domain.setDevice(device);
        }
        if (entity.getPoints() != null) {
            domain.setPoints(entity.getPoints().stream().map(p -> {
                Point point = new Point();
                pointConverter.toDomainObject(p, point);
                return point;
            }).collect(Collectors.toList()));
        }
        super.toDomainObject(entity, domain);
    }

    @Override
    public void toEntityObject(Scan domain, ScanEntity entity) {
        if (domain.getUserLocation() != null) {
            UserLocationEntity userLocationEntity = new UserLocationEntity();
            userLocationConverter.toEntityObject(domain.getUserLocation(), userLocationEntity);
            entity.setUserLocation(userLocationEntity);
        }
        if (domain.getDevice() != null) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceConverter.toEntityObject(domain.getDevice(), deviceEntity);
            entity.setDevice(deviceEntity);
        }
        if (domain.getPoints() != null) {
            entity.setPoints(domain.getPoints().stream().map(p -> {
                PointEntity pointEntity = new PointEntity();
                pointConverter.toEntityObject(p, pointEntity);
                return pointEntity;
            }).collect(Collectors.toList()));
        }
        super.toEntityObject(domain, entity);
    }
}
