package com.hton.dao;

import com.hton.domain.Scan;
import com.hton.entities.ScanEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScanDao extends CommonDao<Scan, ScanEntity> {

    @Override
    public Class<ScanEntity> getEntityClass() {
        return ScanEntity.class;
    }

    @Override
    public void remove(String id, List<String> joinIds) {
        // do nothing
    }
}
