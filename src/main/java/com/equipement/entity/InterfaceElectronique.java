package com.equipement.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "InterfaceElectronique")
public class InterfaceElectronique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInterface;

    private String nom;
    private Date dateCreation;
    private String observations;



    @ManyToOne
    @JoinColumn(name = "idCompte")
    private Compte compte;

    @OneToMany(mappedBy = "interfaceElectronique", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fichier> fichiers;

    // Getters et Setters
    public Long getIdInterface() { return idInterface; }
    public void setIdInterface(Long idInterface) { this.idInterface = idInterface; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Compte getCompte() { return compte; }
    public void setCompte(Compte compte) { this.compte = compte; }
    public List<Fichier> getFichiers() { return fichiers; }
    public void setFichiers(List<Fichier> fichiers) { this.fichiers = fichiers; }
}
