package com.equipement.controller;

import com.equipement.entity.Fichier;
import com.equipement.services.FichierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/fichiers")
public class FichierController {

    @Autowired
    private FichierService fichierService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFichier(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long idInterface,
            @RequestParam String typeFichier) {
        fichierService.upload(file, idInterface, typeFichier);
        return ResponseEntity.ok("Fichier uploadé avec succès");
    }

    @GetMapping("/{idFichier}/telecharger")
    public ResponseEntity<Resource> telechargerFichier(@PathVariable Long idFichier) {
        Fichier fichier = fichierService.getFichierById(idFichier);
        Path filePath = Paths.get(fichier.getCheminFichier());
        Resource resource;
        try {
            resource = new InputStreamResource(Files.newInputStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fichier.getNom() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{idFichier}")
    public ResponseEntity<String> supprimerFichier(@PathVariable Long idFichier) {
        fichierService.supprimer(idFichier);
        return ResponseEntity.ok("Fichier supprimé avec succès");
    }
}
