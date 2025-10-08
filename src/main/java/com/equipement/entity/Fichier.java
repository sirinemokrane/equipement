package com.equipement.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Fichier")
public class Fichier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFichier;
    private String nom;
    private String typeFichier;
    private Long taille;
    private Date dateUpload;
    private String cheminFichier;

    @ManyToOne
    @JoinColumn(name = "idInterface")
    private InterfaceElectronique interfaceElectronique;

    @ManyToMany
    @JoinTable(
            name = "fichier_cable",
            joinColumns = @JoinColumn(name = "idFichier"),
            inverseJoinColumns = @JoinColumn(name = "idCable")
    )
    private List<Cable> cables;

    // Getters et Setters
    public Long getIdFichier() { return idFichier; }
    public void setIdFichier(Long idFichier) { this.idFichier = idFichier; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTypeFichier() { return typeFichier; }
    public void setTypeFichier(String typeFichier) { this.typeFichier = typeFichier; }
    public Long getTaille() { return taille; }
    public void setTaille(Long taille) { this.taille = taille; }
    public Date getDateUpload() { return dateUpload; }
    public void setDateUpload(Date dateUpload) { this.dateUpload = dateUpload; }
    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
    public InterfaceElectronique getInterfaceElectronique() { return interfaceElectronique; }
    public void setInterfaceElectronique(InterfaceElectronique interfaceElectronique) { this.interfaceElectronique = interfaceElectronique; }
    public List<Cable> getCables() { return cables; }
    public void setCables(List<Cable> cables) { this.cables = cables; }

    // Méthode pour valider le fichier
    public boolean valider() {
        return this.nom != null && !this.nom.trim().isEmpty() &&
                this.typeFichier != null && !this.typeFichier.trim().isEmpty() &&
                this.taille != null && this.taille > 0 &&
                this.cheminFichier != null && !this.cheminFichier.trim().isEmpty();
    }
}
