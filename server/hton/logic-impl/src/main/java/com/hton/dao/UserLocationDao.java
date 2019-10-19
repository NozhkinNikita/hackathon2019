package com.hton.dao;

import com.hton.domain.UserLocation;
import com.hton.entities.UserLocationEntity;
import org.springframework.stereotype.Component;

@Component
public class UserLocationDao extends CommonDao<UserLocation, UserLocationEntity> {
    @Override
    public Class<UserLocationEntity> getEntityClass() {
        return UserLocationEntity.class;
    }
}
