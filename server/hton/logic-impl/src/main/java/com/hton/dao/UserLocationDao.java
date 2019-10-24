package com.hton.dao;

import com.hton.domain.UserLocation;
import com.hton.entities.UserLocationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserLocationDao extends CommonDao<UserLocation, UserLocationEntity> {
    @Override
    public Class<UserLocationEntity> getEntityClass() {
        return UserLocationEntity.class;
    }

    @Override
    public void remove(String id, List<String> joinIds) {
        // do nothing
    }

    @Override
    public void update(UserLocation domain, List<String> removeIds) {
        // do nothing
    }
}
