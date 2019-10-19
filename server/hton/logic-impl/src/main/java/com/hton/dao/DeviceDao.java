package com.hton.dao;

import com.hton.domain.Device;
import com.hton.entities.DeviceEntity;
import org.springframework.stereotype.Component;

@Component
public class DeviceDao extends CommonDao<Device, DeviceEntity> {
    @Override
    public Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }
}
