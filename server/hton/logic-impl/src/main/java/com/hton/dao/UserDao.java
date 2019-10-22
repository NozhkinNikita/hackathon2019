package com.hton.dao;

import com.hton.domain.User;
import com.hton.entities.UserEntity;
import com.hton.entities.UserLocationEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Component
public class UserDao extends CommonDao<User, UserEntity> {

    @Override
    public Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    public void remove(String userId, List<String> userLocationIds) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            userLocationIds.forEach(ulId -> {
                UserLocationEntity userLocationEntity = em.find(UserLocationEntity.class, ulId);
                em.remove(userLocationEntity);
            });
            UserEntity entity = em.find(UserEntity.class, userId);
            em.remove(entity);
        } finally {
            transaction.commit();
            em.close();
        }
    }
}
