package com.hton.dao;

import com.hton.domain.Location;
import com.hton.entities.LocationEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Component
public class LocationDao extends CommonDao<Location, LocationEntity> {
    @Override
    public Class<LocationEntity> getEntityClass() {
        return LocationEntity.class;
    }

    @Override
    public void remove(String locationId, List<String> userLocationIds) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            userLocationIds.forEach(ulId -> {
                UserLocationEntity userLocationEntity = em.find(UserLocationEntity.class, ulId);
                em.remove(userLocationEntity);
            });
            LocationEntity entity = em.find(LocationEntity.class, locationId);
            em.remove(entity);
        } finally {
            transaction.commit();
            em.close();
        }
    }

    @Override
    public void update(Location domain, List<String> removeIds) {
        // do nothing
    }
}
