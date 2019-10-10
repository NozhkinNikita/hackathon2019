package com.hton.converters;

import com.hton.dao.CommonDao;
import com.hton.domain.Point;
import com.hton.entities.PointEntity;
import org.springframework.stereotype.Component;

@Component
public class PointDao extends CommonDao<Point, PointEntity> {
    @Override
    public Class<PointEntity> getEntityClass() {
        return PointEntity.class;
    }
}
