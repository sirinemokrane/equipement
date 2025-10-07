package com.equipement.dto;

import java.util.Date;
import java.util.List;

public class FichierDTO {
    private Long idFichier;
    private String nom;
    private String typeFichier;
    private Long taille;
    private Date dateUpload;
    private String cheminFichier;
    private Long idInterface;
    private List<Long> cableIds;

    public FichierDTO() {}

    public FichierDTO(Long idFichier, String nom, String typeFichier, Long taille, Date dateUpload, String cheminFichier, Long idInterface, List<Long> cableIds) {
        this.idFichier = idFichier;
        this.nom = nom;
        this.typeFichier = typeFichier;
        this.taille = taille;
        this.dateUpload = dateUpload;
        this.cheminFichier = cheminFichier;
        this.idInterface = idInterface;
        this.cableIds = cableIds;
    }

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
    public Long getIdInterface() { return idInterface; }
    public void setIdInterface(Long idInterface) { this.idInterface = idInterface; }
    public List<Long> getCableIds() { return cableIds; }
    public void setCableIds(List<Long> cableIds) { this.cableIds = cableIds; }
}
