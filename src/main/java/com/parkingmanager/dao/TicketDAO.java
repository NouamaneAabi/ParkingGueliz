package com.parkingmanager.dao;

import com.parkingmanager.model.Ticket;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class TicketDAO extends GenericDAO<Ticket> {
    public TicketDAO() {
        super(Ticket.class);
    }

    public List<Ticket> findTicketsOuverts() {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Ticket> query = session.createQuery("from Ticket where sortie is null", Ticket.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Ticket findTicketOuvertByVehicule(Long vehiculeId) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Ticket> query = session.createQuery("from Ticket where vehicule.id = :vehiculeId and sortie is null", Ticket.class);
            query.setParameter("vehiculeId", vehiculeId);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    public List<Ticket> findTicketsByDate(LocalDateTime dateDebut, LocalDateTime dateFin) {
        Session session = GenericDAO.getSessionFactory().openSession();
        try {
            Query<Ticket> query = session.createQuery(
                "from Ticket where entree >= :dateDebut and entree <= :dateFin", Ticket.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } finally {
            session.close();
        }
    }
}





