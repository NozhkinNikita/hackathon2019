package com.hton.dao;

import com.hton.domain.Point;
import com.hton.domain.RouterData;
import com.hton.entities.PointEntity;
import com.hton.entities.RouterDataEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

    @Override
    public void update(Point domain, List<String> removeRouterDataIds) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            PointEntity entity = getEntityClass().newInstance();

            removeRouterDataIds.forEach(removeRouterDataId -> {
                RouterDataEntity routerDataEntity = em.find(RouterDataEntity.class, removeRouterDataId);
                em.remove(routerDataEntity);
            });

            converter.toEntityObject(domain, entity);

            em.merge(entity);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        } finally {
            transaction.commit();
            em.close();
        }
    }
}
