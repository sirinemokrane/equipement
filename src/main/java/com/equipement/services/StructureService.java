package com.equipement.services;

import com.equipement.entity.Compte;
import com.equipement.entity.Structure;
import com.equipement.Repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StructureService {

    @Autowired
    private StructureRepository structureRepository;

    // Méthodes CRUD de base
    public List<Structure> getAllStructures() {
        return structureRepository.findAll();
    }

    public Structure getStructureById(Long id) {
        return structureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));
    }

    public Structure saveStructure(Structure structure) {
        return structureRepository.save(structure);
    }

    public void deleteStructure(Long id) {
        structureRepository.deleteById(id);
    }

    // Méthode pour ajouter un compte à une structure
    public void ajouterCompte(Long idStructure, Compte compte) {
        Objects.requireNonNull(compte, "Le compte ne peut pas être null");

        Structure structure = structureRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        // Ajouter le compte à la liste des comptes de la structure
        structure.getComptes().add(compte);

        // Mettre à jour la référence de la structure dans le compte
        compte.setStructure(structure);

        // Sauvegarder les modifications
        structureRepository.save(structure);
    }

    // Méthode pour supprimer un compte d'une structure
    public void supprimerCompte(Long idStructure, Long idCompte) {
        Structure structure = structureRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        // Trouver le compte à supprimer
        Compte compteASupprimer = structure.getComptes().stream()
                .filter(compte -> compte.getIdCompte().equals(idCompte))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Compte non trouvé dans cette structure"));

        // Supprimer le compte de la liste des comptes de la structure
        structure.getComptes().remove(compteASupprimer);

        // Mettre à jour la référence de la structure dans le compte (optionnel)
        compteASupprimer.setStructure(null);

        // Sauvegarder les modifications
        structureRepository.save(structure);
    }

    // Méthode pour lister les comptes d'une structure
    public List<Compte> listerComptes(Long idStructure) {
        Structure structure = structureRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        return structure.getComptes();
    }
}
