package com.parkingmanager.model;

import javax.persistence.*;

@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false, length = 20)
    private String statut; // "LIBRE" ou "OCCUPEE"

    public Place() {
    }

    public Place(Integer numero, String statut) {
        this.numero = numero;
        this.statut = statut;
    }

    public Place(Long id, Integer numero, String statut) {
        this.id = id;
        this.numero = numero;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Place{id=" + id + ", numero=" + numero + ", statut='" + statut + "'}";
    }
}
