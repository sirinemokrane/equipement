// Dans une application Spring Batch, le FlatFileItemReader est utilisé pour lire un fichier (CSV, TXT, etc.)
// ligne par ligne. Normalement, le chemin du fichier est configuré statiquement dans la configuration du reader
// (par exemple, avec @Value ou un chemin fixe).

//Problème :
//Si vous voulez que votre job puisse lire différents fichiers à chaque exécution (par exemple, un fichier uploadé par
// l’utilisateur), vous ne pouvez pas utiliser un chemin fixe. Il faut donc configurer dynamiquement le chemin du fichier
// avant que le job ne commence à lire.

//Solution :
//C’est là qu’intervient le DynamicReaderListener. C’est un écouteur (listener) qui s’exécute avant le démarrage du step,
// et qui permet de configurer dynamiquement le FlatFileItemReader avec le chemin du fichier passé en paramètre.

package com.equipement.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

// Écouteur qui configure dynamiquement le reader avant le step
@Component // Indique à Spring que cette classe est un bean géré par le conteneur.
@StepScope  // Permet d'accéder aux job parameters  et garantit qu’une nouvelle instance est créée pour chaque step.
public class DynamicReaderListener implements StepExecutionListener {

    @Autowired
    private FlatFileItemReader cableReader;  // Injecte le reader (sera configuré)

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Récupère les job parameters
        JobParameters jobParameters = stepExecution.getJobParameters();
        String filePath = jobParameters.getString("filePath");//  Extrait la valeur du paramètre filePath

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le paramètre 'filePath' est requis pour configurer le reader.");
        }

        // Vérifie que le fichier existe
        FileSystemResource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            try {
                throw new FileNotFoundException("Fichier non trouvé : " + filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        // Configure dynamiquement le resource du reader
        cableReader.setResource(resource);
        System.out.println("Reader configuré dynamiquement pour le fichier : " + filePath);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Step terminé. Exit status : " + stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }
}