package com.equipement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Cable")
public class Cable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCable;

    private String numSerie;
    private boolean etat;



    @ManyToMany(mappedBy = "cables")
    private List<Fichier> fichiers;

    // Getters et Setters
    public Long getIdCable() { return idCable; }
    public void setIdCable(Long idCable) { this.idCable = idCable; }
    public String getNumSerie() { return numSerie; }
    public void setNumSerie(String numSerie) { this.numSerie = numSerie; }
    public boolean isEtat() { return etat; }
    public void setEtat(boolean etat) { this.etat = etat; }
    public List<Fichier> getFichiers() { return fichiers; }
    public void setFichiers(List<Fichier> fichiers) { this.fichiers = fichiers; }
}
