package com.equipement.controller;

import com.equipement.entity.Compte;
import com.equipement.entity.Structure;
import com.equipement.dto.StructureDTO;
import com.equipement.dto.CompteDTO;
import com.equipement.services.StructureService;
import com.equipement.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/structures")
public class StructureController {

    @Autowired
    private StructureService structureService;

    @Autowired
    private CompteService compteService;

    @GetMapping
    public ResponseEntity<List<StructureDTO>> getAllStructures() {
        List<Structure> structures = structureService.getAllStructures();
        List<StructureDTO> dtos = structures.stream().map(s -> {
            StructureDTO dto = new StructureDTO();
            dto.setIdStructure(s.getIdStructure());
            dto.setNom(s.getNom());
            dto.setLocalisation(s.getLocalisation());
            dto.setTypeStructure(s.getTypeStructure());
            if (s.getComptes() != null) dto.setCompteIds(s.getComptes().stream().map(Compte::getIdCompte).toList());
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructureDTO> getStructureById(@PathVariable Long id) {
        Structure s = structureService.getStructureById(id);
        StructureDTO dto = new StructureDTO();
        dto.setIdStructure(s.getIdStructure());
        dto.setNom(s.getNom());
        dto.setLocalisation(s.getLocalisation());
        dto.setTypeStructure(s.getTypeStructure());
        if (s.getComptes() != null) dto.setCompteIds(s.getComptes().stream().map(Compte::getIdCompte).toList());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Structure> createStructure(@RequestBody StructureDTO dto) {
        Structure structure = new Structure();
        structure.setNom(dto.getNom());
        structure.setLocalisation(dto.getLocalisation());
        structure.setTypeStructure(dto.getTypeStructure());
        Structure nouvelleStructure = structureService.saveStructure(structure);
        return ResponseEntity.ok(nouvelleStructure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Structure> updateStructure(@PathVariable Long id, @RequestBody StructureDTO dto) {
        Structure structure = new Structure();
        structure.setIdStructure(id);
        structure.setNom(dto.getNom());
        structure.setLocalisation(dto.getLocalisation());
        structure.setTypeStructure(dto.getTypeStructure());
        Structure structureMiseAJour = structureService.saveStructure(structure);
        return ResponseEntity.ok(structureMiseAJour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable Long id) {
        structureService.deleteStructure(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idStructure}/comptes")
    public ResponseEntity<String> ajouterCompte(
            @PathVariable Long idStructure,
            @RequestBody CompteDTO dto) {
        Compte compte = new Compte();
        compte.setNom(dto.getNom());
        compte.setEmail(dto.getEmail());
        compte.setTypeCompte(dto.getTypeCompte());
        compte.setDateCreation(dto.getDateCreation());
        // On peut également utiliser compteService.saveCompte si besoin
        structureService.ajouterCompte(idStructure, compte);
        return ResponseEntity.ok("Compte ajouté avec succès à la structure");
    }

    @DeleteMapping("/{idStructure}/comptes/{idCompte}")
    public ResponseEntity<String> supprimerCompte(
            @PathVariable Long idStructure,
            @PathVariable Long idCompte) {
        structureService.supprimerCompte(idStructure, idCompte);
        return ResponseEntity.ok("Compte supprimé avec succès de la structure");
    }

    @GetMapping("/{idStructure}/comptes")
    public ResponseEntity<List<Compte>> listerComptes(@PathVariable Long idStructure) {
        List<Compte> comptes = structureService.listerComptes(idStructure);
        return ResponseEntity.ok(comptes);
    }
}
