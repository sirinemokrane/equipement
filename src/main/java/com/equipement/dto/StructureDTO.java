package com.equipement.dto;

import java.util.List;

public class StructureDTO {
    private Long idStructure;
    private String nom;
    private String localisation;
    private String typeStructure;
    private List<Long> compteIds;

    public StructureDTO() {}

    public StructureDTO(Long idStructure, String nom, String localisation, String typeStructure, List<Long> compteIds) {
        this.idStructure = idStructure;
        this.nom = nom;
        this.localisation = localisation;
        this.typeStructure = typeStructure;
        this.compteIds = compteIds;
    }

    public Long getIdStructure() { return idStructure; }
    public void setIdStructure(Long idStructure) { this.idStructure = idStructure; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getTypeStructure() { return typeStructure; }
    public void setTypeStructure(String typeStructure) { this.typeStructure = typeStructure; }
    public List<Long> getCompteIds() { return compteIds; }
    public void setCompteIds(List<Long> compteIds) { this.compteIds = compteIds; }
}
