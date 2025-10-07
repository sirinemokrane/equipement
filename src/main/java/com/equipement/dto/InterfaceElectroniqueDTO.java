package com.equipement.dto;

import java.util.Date;
import java.util.List;

public class InterfaceElectroniqueDTO {
    private Long idInterface;
    private String nom;
    private Date dateCreation;
    private String observations;
    private Long idCompte;
    private List<Long> fichierIds;

    public InterfaceElectroniqueDTO() {}

    public InterfaceElectroniqueDTO(Long idInterface, String nom, Date dateCreation, String observations, Long idCompte, List<Long> fichierIds) {
        this.idInterface = idInterface;
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.observations = observations;
        this.idCompte = idCompte;
        this.fichierIds = fichierIds;
    }

    public Long getIdInterface() { return idInterface; }
    public void setIdInterface(Long idInterface) { this.idInterface = idInterface; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public Long getIdCompte() { return idCompte; }
    public void setIdCompte(Long idCompte) { this.idCompte = idCompte; }
    public List<Long> getFichierIds() { return fichierIds; }
    public void setFichierIds(List<Long> fichierIds) { this.fichierIds = fichierIds; }
}
