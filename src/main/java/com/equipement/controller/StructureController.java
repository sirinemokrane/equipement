package com.equipement.controller;

import com.equipement.entity.Compte;
import com.equipement.entity.Structure;
import com.equipement.services.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/structures")
public class StructureController {

    @Autowired
    private StructureService structureService;

    @GetMapping
    public ResponseEntity<List<Structure>> getAllStructures() {
        List<Structure> structures = structureService.getAllStructures();
        return ResponseEntity.ok(structures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Structure> getStructureById(@PathVariable Long id) {
        Structure structure = structureService.getStructureById(id);
        return ResponseEntity.ok(structure);
    }

    @PostMapping
    public ResponseEntity<Structure> createStructure(@RequestBody Structure structure) {
        Structure nouvelleStructure = structureService.saveStructure(structure);
        return ResponseEntity.ok(nouvelleStructure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Structure> updateStructure(@PathVariable Long id, @RequestBody Structure structure) {
        structure.setIdStructure(id);
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
            @RequestBody Compte compte) {
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
