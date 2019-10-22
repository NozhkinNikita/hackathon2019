package com.hton.dao;

import com.hton.domain.Device;
import com.hton.entities.DeviceEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceDao extends CommonDao<Device, DeviceEntity> {
    @Override
    public Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }

    @Override
    public void remove(String id, List<String> joinIds) {
        // do nothing
    }
}
