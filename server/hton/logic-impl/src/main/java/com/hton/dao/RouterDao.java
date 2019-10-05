package com.hton.dao;

import com.hton.domain.Router;
import com.hton.entities.RouterEntity;
import org.springframework.stereotype.Component;

@Component
public class RouterDao extends CommonDao<Router, RouterEntity> {
    @Override
    public Class<RouterEntity> getEntityClass() {
        return RouterEntity.class;
    }
}
