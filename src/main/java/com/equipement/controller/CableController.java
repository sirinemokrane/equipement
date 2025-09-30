package com.equipement.controller;

import com.equipement.entity.Cable;
import com.equipement.services.CableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cables")
public class CableController {

    @Autowired
    private CableService cableService;

    @PostMapping
    public ResponseEntity<String> ajouterCable(@RequestBody Cable cable) {
        cableService.ajouterCable(cable);
        return ResponseEntity.ok("Câble ajouté avec succès");
    }

    @PutMapping("/{idCable}")
    public ResponseEntity<String> modifierCable(
            @PathVariable Long idCable,
            @RequestParam String nouveauNumSerie,
            @RequestParam boolean nouvelEtat) {
        cableService.modifierCable(idCable, nouveauNumSerie, nouvelEtat);
        return ResponseEntity.ok("Câble modifié avec succès");
    }

    @DeleteMapping("/{idCable}")
    public ResponseEntity<String> supprimerCable(@PathVariable Long idCable) {
        cableService.supprimerCable(idCable);
        return ResponseEntity.ok("Câble supprimé avec succès");
    }
}
