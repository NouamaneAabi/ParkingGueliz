package com.parkingmanager.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class GenericDAO<T> {
    protected static SessionFactory sessionFactory;
    private Class<T> entityClass;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de Hibernate: " + e);
            e.printStackTrace();
        }
    }

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void delete(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public T findById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(entityClass, id);
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("from " + entityClass.getName()).list();
        } finally {
            session.close();
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

