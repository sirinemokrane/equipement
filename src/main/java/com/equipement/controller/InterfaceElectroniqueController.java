package com.equipement.controller;

import com.equipement.entity.InterfaceElectronique;
import com.equipement.services.InterfaceElectroniqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interfaces")
public class InterfaceElectroniqueController {

    @Autowired
    private InterfaceElectroniqueService interfaceElectroniqueService;

    @PostMapping
    public ResponseEntity<String> creerInterface(
            @RequestBody InterfaceElectronique interfaceElectronique,
            @RequestParam Long idCompte) {
        interfaceElectroniqueService.creer(interfaceElectronique, idCompte);
        return ResponseEntity.ok("Interface électronique créée avec succès");
    }

    @PutMapping("/{idInterface}")
    public ResponseEntity<String> modifierInterface(
            @PathVariable Long idInterface,
            @RequestParam String nouveauNom,
            @RequestParam(required = false) String nouvellesObservations) {
        interfaceElectroniqueService.modifier(idInterface, nouveauNom, nouvellesObservations);
        return ResponseEntity.ok("Interface électronique modifiée avec succès");
    }

    @DeleteMapping("/{idInterface}")
    public ResponseEntity<String> supprimerInterface(@PathVariable Long idInterface) {
        interfaceElectroniqueService.supprimer(idInterface);
        return ResponseEntity.ok("Interface électronique supprimée avec succès");
    }
}
