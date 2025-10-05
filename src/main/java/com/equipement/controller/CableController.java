package com.equipement.controller;

import com.equipement.entity.Cable;
import com.equipement.services.CableService;
import com.equipement.dto.CableDTO;
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
    public ResponseEntity<String> ajouterCable(@RequestBody CableDTO dto) {
        try {
            // Normalize empty serial to null to allow creation when serialNb is blank
            if (dto.getSerialNb() != null && dto.getSerialNb().trim().isEmpty()) dto.setSerialNb(null);
            System.out.println("[DEBUG] POST /api/cables received DTO serialNb='" + dto.getSerialNb() + "' type='" + dto.getType() + "'");
            Cable cable = new Cable();
            cable.setSerialNb(dto.getSerialNb());
            cable.setType(dto.getType());
            cable.setChannelNb(dto.getChannelNb());
            cable.setLineName(dto.getLineName());
            cable.setPointNb(dto.getPointNb());
            cable.setState(parseState(dto.getState()));
            cable.setAutoTest(dto.getAutoTest());
            cable.setEasting(dto.getEasting());
            cable.setNorthing(dto.getNorthing());
            cable.setElevation(dto.getElevation());
            cable.setNoise(dto.getNoise());
            cable.setDistortion(dto.getDistortion());
            cable.setPhase(dto.getPhase());
            cable.setGain(dto.getGain());
            cable.setVersion(dto.getVersion());
            cable.setLastTestDate(dto.getLastTestDate());
            cable.setCxMaster(dto.getCxMaster());
            // fichiers sont gérés séparément via endpoints fichiers-cables
            cableService.saveRaw(cable);
            return ResponseEntity.ok("Câble ajouté avec succès");
        } catch (Exception e) {
            System.out.println("[ERROR] Exception in POST /api/cables: " + e.getMessage());
            e.printStackTrace(System.out);
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout du câble: " + e.getMessage());
        }
    }

    @PutMapping("/{idCable}")
    public ResponseEntity<String> modifierCable(
            @PathVariable Long idCable,
            @RequestBody CableDTO dto) {
        try {
            Cable cable = new Cable();
            cable.setIdCable(idCable);
            cable.setSerialNb(dto.getSerialNb());
            cable.setType(dto.getType());
            cable.setChannelNb(dto.getChannelNb());
            cable.setLineName(dto.getLineName());
            cable.setPointNb(dto.getPointNb());
            cable.setState(parseState(dto.getState()));
            cable.setAutoTest(dto.getAutoTest());
            cable.setEasting(dto.getEasting());
            cable.setNorthing(dto.getNorthing());
            cable.setElevation(dto.getElevation());
            cable.setNoise(dto.getNoise());
            cable.setDistortion(dto.getDistortion());
            cable.setPhase(dto.getPhase());
            cable.setGain(dto.getGain());
            cable.setVersion(dto.getVersion());
            cable.setLastTestDate(dto.getLastTestDate());
            cable.setCxMaster(dto.getCxMaster());
            cableService.modifierCable(cable);
            return ResponseEntity.ok("Câble modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la modification du câble: " + e.getMessage());
        }
    }

    // Normalize DTO state string to entity state string. Accepts variants and returns canonical "Ok" or "ko".
    private String parseState(String state) {
        if (state == null) return "ko";
        String s = state.trim().replaceAll("\"", "");
        if (s.equalsIgnoreCase("ok") || s.equalsIgnoreCase("true") || s.equals("1")) return "Ok";
        return "ko";
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
