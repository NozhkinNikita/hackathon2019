package com.hton.dao;

import com.hton.domain.Router;
import com.hton.entities.RouterEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterDao extends CommonDao<Router, RouterEntity> {
    @Override
    public Class<RouterEntity> getEntityClass() {
        return RouterEntity.class;
    }

    @Override
    public void remove(String id, List<String> joinIds) {
        // do nothing
    }

    @Override
    public void update(Router domain, List<String> removeIds) {
        // do nothing
    }
}
