package com.equipement.controller;

import com.equipement.entity.Compte;
import com.equipement.entity.TypeCompte;
import com.equipement.services.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    @Autowired
    private CompteService compteService;

    @GetMapping
    public ResponseEntity<List<Compte>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(@PathVariable Long id) {
        Compte compte = compteService.getCompteById(id);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(compte);
    }

    @PostMapping
    public ResponseEntity<Compte> createCompte(@RequestBody Compte compte) {
        Compte nouveauCompte = compteService.saveCompte(compte);
        return ResponseEntity.ok(nouveauCompte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable Long id, @RequestBody Compte compte) {
        compte.setIdCompte(id);
        Compte compteMisAJour = compteService.saveCompte(compte);
        return ResponseEntity.ok(compteMisAJour);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<List<Compte>> getComptesByStructure(@PathVariable Long idStructure) {
        List<Compte> comptes = compteService.getComptesByStructure(idStructure);
        return ResponseEntity.ok(comptes);
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
}
