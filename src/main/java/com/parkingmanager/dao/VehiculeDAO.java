package com.parkingmanager.dao;

import com.parkingmanager.model.Vehicule;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class VehiculeDAO extends GenericDAO<Vehicule> {
    public VehiculeDAO() {
        super(Vehicule.class);
    }

    public Vehicule findByImmatriculation(String immatriculation) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Vehicule> query = session.createQuery("from Vehicule where immatriculation = :immatriculation", Vehicule.class);
            query.setParameter("immatriculation", immatriculation);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}





