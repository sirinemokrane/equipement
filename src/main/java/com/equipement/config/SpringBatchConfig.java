// Cette classe est le cœur du traitement batch : elle dit à Spring comment lire, transformer et écrire les données.
package com.equipement.config;

import com.equipement.Repository.CableRepository;
import com.equipement.batch.CableFieldSetMapper;
import com.equipement.batch.CableProcessor;
import com.equipement.batch.CableWriter;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class SpringBatchConfig {

    private final JobRepository jobRepository; // gère l’état et l’historique des jobs (par ex. combien d’enregistrements ont été lus, si le job a échoué, etc.).
    private final PlatformTransactionManager transactionManager; // gère les transactions (commits et rollbacks) pour garantir la cohérence des données.

    public SpringBatchConfig(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }


    // “Spring, crée-moi un objet CableWriter une fois pour tout le programme, et garde-le en mémoire pour le job batch.”
    //
    //Donc oui, déclarer un bean, c’est comme créer un objet,
    //mais en demandant à Spring de le faire et de le gérer à ta place.

    @Bean
    public FlatFileItemReader<Cable> cableReader() {
        // Tokenizer personnalisé pour gérer les guillemets imbriqués
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer() {
            @Override
            protected List<String> doTokenize(String line) {
                // Enlever les guillemets externes si présents
                String cleanLine = line.trim();
                if (cleanLine.startsWith("\"") && cleanLine.endsWith("\"")) {
                    cleanLine = cleanLine.substring(1, cleanLine.length() - 1);
                }

                // Remplacer les doubles guillemets par des guillemets simples
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

        return new FlatFileItemReaderBuilder<Cable>()
                .name("cableItemReader")
                .resource(new ClassPathResource("data/equipement"))
                .linesToSkip(1) // Skip header
                .lineTokenizer(tokenizer)
                .fieldSetMapper(new CableFieldSetMapper())
                .strict(true)
                .build();
    }



    @Bean
    public CableProcessor cableProcessor() {
        return new CableProcessor();
    }

    @Bean
    public CableWriter cableWriter(CableRepository repository) {
        return new CableWriter(repository);
    }

    @Bean
    public Step importCableStep(CableWriter cableWriter) {
        return new StepBuilder("importCableStep", jobRepository)
                .<Cable, Cable>chunk(50, transactionManager)
                .reader(cableReader())
                .processor(cableProcessor())
                .writer(cableWriter)
                .faultTolerant()// permet de gérer les erreurs sans tout arrêter.
                .skip(Exception.class)
                .skipLimit(10) // Maximum 10 erreurs tolérées
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