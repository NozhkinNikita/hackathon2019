package com.hton.dao;

import com.hton.domain.Point;
import com.hton.entities.PointEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointDao extends CommonDao<Point, PointEntity> {
    @Override
    public Class<PointEntity> getEntityClass() {
        return PointEntity.class;
    }

    @Override
    public void remove(String id, List<String> joinIds) {
        // do nothing
    }
}
