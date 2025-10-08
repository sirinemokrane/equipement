package com.equipement.dto;

import com.equipement.entity.TypeCompte;

import java.util.Date;

public class CompteDTO {
    private Long idCompte;
    private String nom;
    private String email;
    private TypeCompte typeCompte;
    private Date dateCreation;
    private Long idStructure;

    public CompteDTO() {}

    public CompteDTO(Long idCompte, String nom, String email, TypeCompte typeCompte, Date dateCreation, Long idStructure) {
        this.idCompte = idCompte;
        this.nom = nom;
        this.email = email;
        this.typeCompte = typeCompte;
        this.dateCreation = dateCreation;
        this.idStructure = idStructure;
    }

    public Long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getIdStructure() {
        return idStructure;
    }

    public void setIdStructure(Long idStructure) {
        this.idStructure = idStructure;
    }
}
