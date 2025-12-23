package com.parkingmanager.dao;

import com.parkingmanager.model.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ConfigurationDAO extends GenericDAO<Configuration> {
    public ConfigurationDAO() {
        super(Configuration.class);
    }

    public String getValue(String cle, String defaultValue) {
        try (Session session = sessionFactory.openSession()) {
            Query<Configuration> query = session.createQuery("from Configuration where cle = :cle", Configuration.class);
            query.setParameter("cle", cle);
            Configuration config = query.uniqueResult();
            return config != null ? config.getValeur() : defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public void setValue(String cle, String valeur) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Configuration> query = session.createQuery("from Configuration where cle = :cle", Configuration.class);
            query.setParameter("cle", cle);
            Configuration config = query.uniqueResult();
            
            if (config == null) {
                config = new Configuration(cle, valeur);
                session.save(config);
            } else {
                config.setValeur(valeur);
                session.update(config);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
