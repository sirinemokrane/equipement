package com.equipement.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batch")
public class BatchLauncher {

    private final JobLauncher jobLauncher;
    private final Job importCableJob;

    public BatchLauncher(JobLauncher jobLauncher, Job importCableJob) {
        this.jobLauncher = jobLauncher;
        this.importCableJob = importCableJob;
    }

    // MODIFIÉ : Ajoute paramètre filePath (optionnel pour compatibilité)
    @GetMapping("/import-cables")
    public String importCables(
            @RequestParam(defaultValue = "false") boolean clearFirst,
            @RequestParam(required = false) String filePath  // NOUVEAU : Chemin du fichier dynamique
    ) {
        try {
            JobParametersBuilder builder = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .addString("clearFirst", String.valueOf(clearFirst));

            // AJOUTÉ : Si filePath fourni, l'ajouter comme paramètre pour le reader
            if (filePath != null && !filePath.trim().isEmpty()) {
                builder.addString("filePath", filePath);
            } else {
                // Fallback pour ancien usage statique (optionnel : tu peux le supprimer)
                builder.addString("filePath", "data/equipement");  // Ancien chemin relatif
            }

            JobParameters params = builder.toJobParameters();

            jobLauncher.run(importCableJob, params);

            return "Batch job started successfully! Fichier: " + (filePath != null ? filePath : "statique");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error starting batch job: " + e.getMessage();
        }
    }
}