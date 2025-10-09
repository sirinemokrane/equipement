package com.equipement.controller;

import com.equipement.dto.CableDTO;
import com.equipement.entity.Cable;
import com.equipement.services.CableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cables")
@Tag(name = "Câbles", description = "Gestion des câbles de l'équipement")
@CrossOrigin(origins = "*")
public class CableController {

    @Autowired
    private CableService cableService;

    @Operation(summary = "Test de connexion", description = "Endpoint simple pour tester que l'API fonctionne")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API Câbles - OK ✓");
    }

    @Operation(summary = "Récupérer tous les câbles", description = "Retourne la liste complète des câbles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping
    public ResponseEntity<List<CableDTO>> getAllCables() {
        try {
            List<Cable> cables = cableService.getAllCables();
            List<CableDTO> cableDTOs = cables.stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cableDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ArrayList<>());
        }
    }

    @Operation(summary = "Récupérer un câble par ID", description = "Retourne les détails d'un câble spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Câble trouvé"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Câble non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CableDTO> getCableById(
            @Parameter(description = "ID du câble", required = true)
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Cable cable = cableService.getCableById(id);
            CableDTO dto = entityToDTO(cable);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Créer un nouveau câble", description = "Ajoute un câble dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Câble créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    public ResponseEntity<CableDTO> createCable(
            @Parameter(description = "Données du câble à créer", required = true)
            @RequestBody CableDTO cableDTO) {
        try {
            if (cableDTO == null || cableDTO.getSerialNb() == null) {
                return ResponseEntity.badRequest().build();
            }
            Cable cable = dtoToEntity(cableDTO);
            cable.setIdCable(null);  // Force création
            cableService.ajouterCable(cable);
            CableDTO savedDTO = entityToDTO(cable);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Modifier un câble existant", description = "Met à jour les informations d'un câble")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Câble modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou ID mismatch"),
            @ApiResponse(responseCode = "404", description = "Câble non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CableDTO> updateCable(
            @Parameter(description = "ID du câble à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données du câble", required = true)
            @RequestBody CableDTO cableDTO) {
        try {
            if (id == null || id <= 0 || cableDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            if (!Objects.equals(id, cableDTO.getIdCable())) {
                return ResponseEntity.badRequest().build();
            }
            Cable cable = dtoToEntity(cableDTO);
            cableService.modifierCable(cable);
            CableDTO updatedDTO = entityToDTO(cable);
            return ResponseEntity.ok(updatedDTO);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvé")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Supprimer un câble", description = "Supprime un câble de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Câble supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "ID invalide"),
            @ApiResponse(responseCode = "404", description = "Câble non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCable(
            @Parameter(description = "ID du câble à supprimer", required = true)
            @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            cableService.supprimerCable(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvé")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== MÉTHODES DE MAPPING ==========

    private CableDTO entityToDTO(Cable cable) {
        CableDTO dto = new CableDTO();
        dto.setIdCable(cable.getIdCable());
        dto.setType(cable.getType());
        dto.setSerialNb(cable.getSerialNb());
        dto.setChannelNb(cable.getChannelNb());
        dto.setLineName(cable.getLineName());
        dto.setPointNb(cable.getPointNb());
        dto.setState(cable.getState());
        dto.setAutoTest(cable.getAutoTest());
        dto.setEasting(cable.getEasting());
        dto.setNorthing(cable.getNorthing());
        dto.setElevation(cable.getElevation());
        dto.setNoise(cable.getNoise());
        dto.setDistortion(cable.getDistortion());
        dto.setPhase(cable.getPhase());
        dto.setGain(cable.getGain());
        dto.setVersion(cable.getVersion());
        dto.setLastTestDate(cable.getLastTestDate());
        dto.setCxMaster(cable.getCxMaster());
        dto.setFichierIds(new ArrayList<>());
        return dto;
    }

    private Cable dtoToEntity(CableDTO dto) {
        Cable cable = new Cable();
        cable.setIdCable(dto.getIdCable());
        cable.setType(dto.getType());
        cable.setSerialNb(dto.getSerialNb());
        cable.setChannelNb(dto.getChannelNb());
        cable.setLineName(dto.getLineName());
        cable.setPointNb(dto.getPointNb());
        cable.setState(dto.getState());
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
        return cable;
    }
}