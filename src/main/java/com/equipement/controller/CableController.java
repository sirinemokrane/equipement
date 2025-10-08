package com.equipement.controller;

import com.equipement.dto.CableDTO;
import com.equipement.entity.Cable;
import com.equipement.services.CableService;
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
@CrossOrigin(origins = "*")  // Optionnel : Pour tester avec frontend (retire en prod)
public class CableController {

    @Autowired
    private CableService cableService;

    // READ: Récupérer tous les câbles
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
                    .body(new ArrayList<>());  // Liste vide en cas d'erreur
        }
    }

    // READ: Récupérer un câble par ID
    @GetMapping("/{id}")
    public ResponseEntity<CableDTO> getCableById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Cable cable = cableService.getCableById(id);
            CableDTO dto = entityToDTO(cable);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Ou renvoie un DTO vide avec message
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // CREATE: Ajouter un nouveau câble
    @PostMapping
    public ResponseEntity<CableDTO> createCable(@RequestBody CableDTO cableDTO) {
        try {
            if (cableDTO == null || cableDTO.getSerialNb() == null) {  // Validation basique
                return ResponseEntity.badRequest().build();
            }
            Cable cable = dtoToEntity(cableDTO);
            cable.setIdCable(null);  // Force create (ignore ID si fourni)
            cableService.ajouterCable(cable);  // Utilise ta méthode validée
            CableDTO savedDTO = entityToDTO(cable);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);  // Ex. : "Câble ne peut pas être null"
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE: Modifier un câble existant
    @PutMapping("/{id}")
    public ResponseEntity<CableDTO> updateCable(@PathVariable Long id, @RequestBody CableDTO cableDTO) {
        try {
            if (id == null || id <= 0 || cableDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            if (!Objects.equals(id, cableDTO.getIdCable())) {
                return ResponseEntity.badRequest().body(null);  // ID mismatch
            }
            Cable cable = dtoToEntity(cableDTO);
            cableService.modifierCable(cable);  // Utilise ta méthode merge
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

    // DELETE: Supprimer un câble
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCable(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            cableService.supprimerCable(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvé")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mapper Entity → DTO (manuel, ignore fichiers pour CRUD simple)
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
        dto.setFichierIds(new ArrayList<>());  // Vide pour l'instant (gère relations séparément)
        return dto;
    }

    // Mapper DTO → Entity (manuel)
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
        // Ignore fichiers (ManyToMany) – ajoute une méthode séparée si besoin
        return cable;
    }
}