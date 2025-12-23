package com.parkingmanager.dao;

import com.parkingmanager.model.Paiement;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PaiementDAO extends GenericDAO<Paiement> {
    public PaiementDAO() {
        super(Paiement.class);
    }

    public List<Paiement> findPaiementsByDate(LocalDate date) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            LocalDateTime debut = date.atStartOfDay();
            LocalDateTime fin = date.plusDays(1).atStartOfDay();
            Query<Paiement> query = session.createQuery(
                "from Paiement where date >= :debut and date < :fin", Paiement.class);
            query.setParameter("debut", debut);
            query.setParameter("fin", fin);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Double getRecettesDuJour(LocalDate date) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            LocalDateTime debut = date.atStartOfDay();
            LocalDateTime fin = date.plusDays(1).atStartOfDay();
            Query<Double> query = session.createQuery(
                "select sum(montant) from Paiement where date >= :debut and date < :fin", Double.class);
            query.setParameter("debut", debut);
            query.setParameter("fin", fin);
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        } finally {
            session.close();
        }
    }
}





