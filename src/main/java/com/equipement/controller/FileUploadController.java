//Reçoit un fichier CSV uploadé par l'utilisateur (via une requête HTTP POST).
//Stocke le fichier dans un dossier temporaire sur le serveur.
//Lance le job Spring Batch pour traiter ce fichier (via BatchLauncher).
//Retourne une réponse à l'utilisateur (succès/erreur).


package com.equipement.controller;  // EXACT : Même que BatchLauncher

import com.equipement.controller.BatchLauncher;  // Ton launcher existant
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;  // TEMP : Pour test GET
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private BatchLauncher batchLauncher;

    // TEMPORAIRE : Endpoint GET simple pour tester le scan
    @GetMapping("/test")
    public String test() {
        return "FileUploadController détecté ! Prêt pour upload sur port 9091.";
    }

    @PostMapping("/cables")
    public ResponseEntity<String> uploadCablesFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide !");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Seuls les fichiers CSV sont acceptés !");
        }

        try {
            String uploadDir = System.getProperty("java.io.tmpdir") + "/uploads/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String result = batchLauncher.importCables(false, filePath.toString());

            return ResponseEntity.ok("Upload réussi ! " + result + " (Fichier : " + fileName + ")");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur upload : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur Batch : " + e.getMessage());
        }
    }
}