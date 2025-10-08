package com.equipement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class InterfaceElectroniqueDTO {
    private Long idInterface;
    private String nom;
    private Date dateCreation;
    private String observations;
    private Long idCompte; // ID du compte associé
    private List<Long> fichierIds; // Liste des IDs des fichiers associés

}