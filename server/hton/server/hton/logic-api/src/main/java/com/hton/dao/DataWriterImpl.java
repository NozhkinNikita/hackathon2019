package com.hton.dao;

import com.hton.dao.exceptions.DataWriterException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@Component
public class DataWriterImpl implements DataWriter {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void store(Object entity) throws DataWriterException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DataWriterException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Object entity) throws DataWriterException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.merge(entity);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DataWriterException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void remove(Object entity) throws DataWriterException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(entity);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new DataWriterException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
