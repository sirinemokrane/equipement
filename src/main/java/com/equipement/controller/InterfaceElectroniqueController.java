package com.equipement.controller;

import com.equipement.entity.InterfaceElectronique;
import com.equipement.services.InterfaceElectroniqueService;
import com.equipement.dto.InterfaceElectroniqueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interfaces")
public class InterfaceElectroniqueController {

    @Autowired
    private InterfaceElectroniqueService interfaceElectroniqueService;

    @GetMapping
    public ResponseEntity<List<InterfaceElectronique>> getAllInterfaces() {
        List<InterfaceElectronique> interfaces = interfaceElectroniqueService.getAllInterfaces();
        return ResponseEntity.ok(interfaces);
    }

    @GetMapping("/{idInterface}")
    public ResponseEntity<InterfaceElectronique> getInterfaceById(@PathVariable Long idInterface) {
        try {
            InterfaceElectronique interfaceElectronique = interfaceElectroniqueService.getInterfaceById(idInterface);
            return ResponseEntity.ok(interfaceElectronique);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> creerInterface(
            @RequestBody InterfaceElectroniqueDTO dto,
            @RequestParam(name = "idCompte", required = false) Long idCompteParam) {
        // Map DTO -> entity
        InterfaceElectronique entity = new InterfaceElectronique();
        entity.setNom(dto.getNom());
        entity.setObservations(dto.getObservations());

        // Supporter idCompte soit dans le DTO soit en paramètre de requête (rétrocompatibilité)
        Long idCompte = dto.getIdCompte() != null ? dto.getIdCompte() : idCompteParam;
        if (idCompte == null) {
            return ResponseEntity.badRequest().body("Le paramètre idCompte est requis (dans le body ou en query param)");
        }

        try {
            // la dateCreation est définie dans le service
            interfaceElectroniqueService.creer(entity, idCompte);
            return ResponseEntity.ok("Interface électronique créée avec succès");
        } catch (RuntimeException e) {
            // Retourner un message lisible au client au lieu d'exception 500
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'interface: " + e.getMessage());
        }
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
