package com.equipement.controller;

import com.equipement.entity.Cable;
import com.equipement.services.CableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cables")
public class CableController {

    @Autowired
    private CableService cableService;

    @GetMapping
    public ResponseEntity<List<Cable>> getAllCables() {
        List<Cable> cables = cableService.getAllCables();
        return ResponseEntity.ok(cables);
    }

    @GetMapping("/{idCable}")
    public ResponseEntity<Cable> getCableById(@PathVariable Long idCable) {
        try {
            Cable cable = cableService.getCableById(idCable);
            return ResponseEntity.ok(cable);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> ajouterCable(@RequestBody Cable cable) {
        try {
            cableService.ajouterCable(cable);
            return ResponseEntity.ok("Câble ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout du câble: " + e.getMessage());
        }
    }

    @PutMapping("/{idCable}")
    public ResponseEntity<String> modifierCable(
            @PathVariable Long idCable,
            @RequestBody Cable cable) {
        try {
            cable.setIdCable(idCable);
            cableService.modifierCable(cable);
            return ResponseEntity.ok("Câble modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la modification du câble: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idCable}")
    public ResponseEntity<String> supprimerCable(@PathVariable Long idCable) {
        try {
            cableService.supprimerCable(idCable);
            return ResponseEntity.ok("Câble supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la suppression du câble: " + e.getMessage());
        }
    }
}
