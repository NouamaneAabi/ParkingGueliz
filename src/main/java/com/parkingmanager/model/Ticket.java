package com.parkingmanager.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @Column(nullable = false)
    private LocalDateTime entree;

    @Column
    private LocalDateTime sortie;

    @Column
    private Double montant;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Paiement> paiements;

    public Ticket() {
        this.entree = LocalDateTime.now();
    }

    public Ticket(Place place, Vehicule vehicule) {
        this.place = place;
        this.vehicule = vehicule;
        this.entree = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public LocalDateTime getEntree() {
        return entree;
    }

    public void setEntree(LocalDateTime entree) {
        this.entree = entree;
    }

    public LocalDateTime getSortie() {
        return sortie;
    }

    public void setSortie(LocalDateTime sortie) {
        this.sortie = sortie;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public boolean estOuvert() {
        return sortie == null;
    }
}
