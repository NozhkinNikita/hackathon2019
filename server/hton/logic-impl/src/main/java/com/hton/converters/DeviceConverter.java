package com.hton.converters;

import com.hton.domain.Device;
import com.hton.entities.DeviceEntity;
import org.springframework.stereotype.Component;

@Component
public class DeviceConverter extends Converter<Device, DeviceEntity> {
    @Override
    public Class<Device> getDomainClass() {
        return Device.class;
    }

    @Override
    public Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }
}
