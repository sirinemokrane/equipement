package com.equipement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StructureDTO {
    private Long idStructure;
    private String nom;
    private String localisation;
    private String typeStructure;
    private List<Long> compteIds; // Liste des IDs des comptes associés
}
