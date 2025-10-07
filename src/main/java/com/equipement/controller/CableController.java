package com.equipement.controller;

import com.equipement.entity.Cable;
import com.equipement.services.CableService;
import com.equipement.dto.CableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/cables")
public class CableController {

    @Autowired
    private CableService cableService;

    @Autowired
    private com.equipement.batch.CsvCableImporter csvCableImporter;

    @GetMapping
    public ResponseEntity<List<Cable>> getAllCables() {
        List<Cable> cables = cableService.getAllCables();
        return ResponseEntity.ok(cables);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importCsv(@RequestParam(name = "clear", required = false, defaultValue = "false") boolean clear) {
        try {
            java.util.List<com.equipement.entity.Cable> saved = csvCableImporter.importAndSave(clear);
            return ResponseEntity.ok(java.util.Map.of("imported", saved.size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Import failed: " + e.getMessage());
        }
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

    /**
     * Return first N lines of the CSV file used by the batch. Query param 'lines' optional (default 200).
     */
    @GetMapping("/file")
    public ResponseEntity<?> getFilePreview(@RequestParam(name = "lines", required = false) Integer lines) {
        int n = (lines == null || lines <= 0) ? 200 : lines;
        String path = "src/main/resources/data/equipementt";
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<String> result = stream.limit(n).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read file: " + e.getMessage());
        }
    }

    /**
     * Return parsed CSV rows as a list of objects (header -> value).
     * This reads the classpath resource `/data/equipementt` and handles the "outer-quote + doubled-quote" format.
     */
    @GetMapping("/file-parsed")
    public ResponseEntity<?> getFileParsed() {
        try (InputStream is = getClass().getResourceAsStream("/data/equipementt")) {
            if (is == null) return ResponseEntity.status(500).body("Resource data/equipementt not found");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String headerLine = br.readLine();
                if (headerLine == null) return ResponseEntity.ok(java.util.List.of());
                String[] headers = preprocessLine(headerLine).split(",", -1);
                for (int i = 0; i < headers.length; i++) headers[i] = normalizeToken(headers[i]);
                java.util.List<Map<String, String>> rows = new ArrayList<>();
                String raw;
                while ((raw = br.readLine()) != null) {
                    String[] parts = preprocessLine(raw).split(",", -1);
                    Map<String, String> obj = new LinkedHashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        String v = (i < parts.length) ? normalizeToken(parts[i]) : null;
                        obj.put(headers[i], v);
                    }
                    rows.add(obj);
                }
                return ResponseEntity.ok(rows);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read/parse file: " + e.getMessage());
        }
    }

    private String preprocessLine(String raw) {
        if (raw == null) return "";
        String line = raw.trim();
        if (line.length() >= 2 && line.startsWith("\"") && line.endsWith("\"")) {
            line = line.substring(1, line.length() - 1);
        }
        // unescape doubled quotes
        line = line.replace("\"\"", "\"");
        return line;
    }

    private String normalizeToken(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) t = t.substring(1, t.length() - 1);
        t = t.trim();
        return t.isEmpty() ? null : t;
    }
}
