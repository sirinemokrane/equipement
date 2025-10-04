package com.equipement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Structure")
public class Structure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStructure;

    private String nom;
    private String localisation;
    private String typeStructure;

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Compte> comptes;





    // Getters et Setters
    public Long getIdStructure() { return idStructure; }
    public void setIdStructure(Long idStructure) { this.idStructure = idStructure; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getTypeStructure() { return typeStructure; }
    public void setTypeStructure(String typeStructure) { this.typeStructure = typeStructure; }
    public List<Compte> getComptes() { return comptes; }
    public void setComptes(List<Compte> comptes) { this.comptes = comptes; }


}
