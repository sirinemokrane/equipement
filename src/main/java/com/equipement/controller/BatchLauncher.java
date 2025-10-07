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

    @GetMapping("/import-cables")
    public String importCables(@RequestParam(defaultValue = "false") boolean clearFirst) {
        try {
            //addLong("startTime", System.currentTimeMillis())
            //→ ajoute un identifiant unique basé sur l’heure (obligatoire pour différencier chaque exécution).
            //
            //addString("clearFirst", String.valueOf(clearFirst))
            //→ ajoute le paramètre reçu depuis la requête HTTP (utile si ton job doit adapter son comportement).
            JobParameters params = new JobParametersBuilder()
                    .addLong("startTime", System.currentTimeMillis())
                    .addString("clearFirst", String.valueOf(clearFirst))
                    .toJobParameters();

            jobLauncher.run(importCableJob, params);

            return "Batch job started successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error starting batch job: " + e.getMessage();
        }
    }
}