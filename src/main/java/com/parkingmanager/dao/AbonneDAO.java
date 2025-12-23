package com.parkingmanager.dao;

import com.parkingmanager.model.Abonne;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AbonneDAO extends GenericDAO<Abonne> {
    public AbonneDAO() {
        super(Abonne.class);
    }

    public Abonne findByVehiculeId(Long vehiculeId) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Abonne> query = session.createQuery("from Abonne where vehicule.id = :vehiculeId", Abonne.class);
            query.setParameter("vehiculeId", vehiculeId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    public List<Abonne> findAbonnesActifs() {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Abonne> query = session.createQuery("from Abonne", Abonne.class);
            List<Abonne> abonnes = query.list();
            return abonnes.stream()
                    .filter(Abonne::estActif)
                    .collect(java.util.stream.Collectors.toList());
        } finally {
            session.close();
        }
    }
}

