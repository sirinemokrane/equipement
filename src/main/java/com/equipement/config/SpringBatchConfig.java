package com.equipement.config;

import com.equipement.Repository.CableRepository;
import com.equipement.batch.CableFieldSetMapper;
import com.equipement.batch.CableProcessor;
import com.equipement.batch.CableWriter;
import com.equipement.batch.DynamicReaderListener;  // NOUVEAU : Import du listener
import com.equipement.entity.Cable;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;  // AJOUTÉ : Pour injecter le listener
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;  // Utilisé par le listener
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
// @EnableBatchProcessing est implicite dans Spring Boot, mais tu peux l'ajouter si besoin
public class SpringBatchConfig {

    private final JobRepository jobRepository;//  Stocke l’état des jobs et des steps (métadonnées).
    private final PlatformTransactionManager transactionManager;// Gère les transactions pour les opérations en base de données.

    @Autowired
    @Lazy
    private DynamicReaderListener dynamicReaderListener;  // Injecté pour configurer dynamiquement le reader
    @Bean
    public DynamicReaderListener dynamicReaderListener() {
        return new DynamicReaderListener();
    }

    public SpringBatchConfig(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    // MODIFIÉ : Reader simple (sans @Value ni @StepScope). Resource sera set par le listener
    @Bean
    public FlatFileItemReader<Cable> cableReader() {
        // Tokenizer personnalisé (INCHANGÉ : ta logique pour guillemets)
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer() {
            @Override
            protected List<String> doTokenize(String line) {
                String cleanLine = line.trim();
                if (cleanLine.startsWith("\"") && cleanLine.endsWith("\"")) {
                    cleanLine = cleanLine.substring(1, cleanLine.length() - 1);
                }
                cleanLine = cleanLine.replace("\"\"", "\"");
                return List.of(super.doTokenize(cleanLine).toArray(new String[0]));
            }
        };

        tokenizer.setDelimiter(",");
        tokenizer.setQuoteCharacter('"');
        tokenizer.setNames(
                "type", "serialNb", "channelNb", "lineName", "pointNb",
                "state", "autoTest", "easting", "northing", "elevation",
                "noise", "distortion", "phase", "gain", "version",
                "lastTestDate", "cxMaster"
        );

        FlatFileItemReader<Cable> reader = new FlatFileItemReaderBuilder<Cable>()
                .name("cableItemReader")
                .linesToSkip(1) // Skip header
                .lineTokenizer(tokenizer)
                .fieldSetMapper(new CableFieldSetMapper())
                .strict(true)
                .build();

        // Resource initial : Un placeholder (sera overwritten par le listener)
        // Tu peux set un fichier vide ou null ; le listener le remplacera
        reader.setResource(new FileSystemResource(""));  // Vide pour l'instant

        return reader;
    }

    // Les autres beans INCHANGÉS
    @Bean
    public CableProcessor cableProcessor() { //Rôle : Traiter chaque objet Cable après sa lecture (validation, transformation, enrichissement, etc.).
        return new CableProcessor();
    }

    @Bean
    public CableWriter cableWriter(CableRepository repository) { // Rôle : Écrire les objets Cable en base de données.
        return new CableWriter(repository);
    }

    // MODIFIÉ : Step avec le listener injecté
    @Bean
    public Step importCableStep(FlatFileItemReader<Cable> cableReader, CableWriter cableWriter) {
        return new StepBuilder("importCableStep", jobRepository)
                .<Cable, Cable>chunk(50, transactionManager)
                .reader(cableReader)
                .processor(cableProcessor())
                .writer(cableWriter)
                .listener(dynamicReaderListener)  // NOUVEAU : Ajoute le listener pour config dynamique
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }

    @Bean
    public Job importCableJob(Step importCableStep) {
        return new JobBuilder("importCableJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importCableStep)
                .build();
    }
}