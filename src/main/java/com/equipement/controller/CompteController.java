package com.equipement.controller;

import com.equipement.entity.Compte;
import com.equipement.entity.TypeCompte;
import com.equipement.dto.CompteDTO;
import com.equipement.services.CompteService;
import com.equipement.services.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    private final StructureService structureService;

    public CompteController(CompteService compteService, StructureService structureService) {
        this.compteService = compteService;
        this.structureService = structureService;
    }

    @GetMapping
    public ResponseEntity<List<CompteDTO>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        List<CompteDTO> dtos = comptes.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompteDTO> getCompteById(@PathVariable Long id) {
        Compte compte = compteService.getCompteById(id);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(compte));
    }

    @PostMapping
    public ResponseEntity<CompteDTO> createCompte(@RequestBody CompteDTO dto) {
        Compte compte = new Compte();
        compte.setNom(dto.getNom());
        compte.setEmail(dto.getEmail());
        compte.setTypeCompte(dto.getTypeCompte());
        compte.setDateCreation(dto.getDateCreation());
        // if structure id provided, fetch and set
        if (dto.getIdStructure() != null) {
            compte.setStructure(structureService.getStructureById(dto.getIdStructure()));
        }
        Compte nouveauCompte = compteService.saveCompte(compte);
        return ResponseEntity.ok(toDTO(nouveauCompte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompteDTO> updateCompte(@PathVariable Long id, @RequestBody CompteDTO dto) {
        Compte compte = new Compte();
        compte.setIdCompte(id);
        compte.setNom(dto.getNom());
        compte.setEmail(dto.getEmail());
        compte.setTypeCompte(dto.getTypeCompte());
        compte.setDateCreation(dto.getDateCreation());
        if (dto.getIdStructure() != null) {
            compte.setStructure(structureService.getStructureById(dto.getIdStructure()));
        }
        Compte compteMisAJour = compteService.saveCompte(compte);
        return ResponseEntity.ok(toDTO(compteMisAJour));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<List<CompteDTO>> getComptesByStructure(@PathVariable Long idStructure) {
        List<Compte> comptes = compteService.getComptesByStructure(idStructure);
        List<CompteDTO> dtos = comptes.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/changer-mot-de-passe")
    public ResponseEntity<String> changerMotDePasse(
            @PathVariable Long id,
            @RequestParam String ancienMotDePasse,
            @RequestParam String nouveauMotDePasse) {
        compteService.changerMotDePasse(id, ancienMotDePasse, nouveauMotDePasse);
        return ResponseEntity.ok("Mot de passe changé avec succès");
    }

    @PutMapping("/{id}/profil")
    public ResponseEntity<String> mettreAJourProfil(
            @PathVariable Long id,
            @RequestParam String nouveauNom,
            @RequestParam String nouvelEmail) {
        compteService.mettreAJourProfil(id, nouveauNom, nouvelEmail);
        return ResponseEntity.ok("Profil mis à jour avec succès");
    }

    @GetMapping("/{id}/role")
    public ResponseEntity<TypeCompte> obtenirRole(@PathVariable Long id) {
        TypeCompte role = compteService.obtenirRole(id);
        return ResponseEntity.ok(role);
    }

    // --- Mapping utility ---
    private CompteDTO toDTO(Compte compte) {
        if (compte == null) return null;
        Long idStructure = null;
        if (compte.getStructure() != null) {
            idStructure = compte.getStructure().getIdStructure();
        }
        return new CompteDTO(
                compte.getIdCompte(),
                compte.getNom(),
                compte.getEmail(),
                compte.getTypeCompte(),
                compte.getDateCreation(),
                idStructure
        );
    }
}
