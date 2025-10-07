package com.equipement.config;

import com.equipement.batch.DBWriter;
import com.equipement.batch.Processor;
import com.equipement.entity.Cable;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.lang.NonNull;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;
import com.equipement.Repository.CableRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	public SpringBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	public FlatFileItemReader<String[]> reader() {
		FlatFileItemReader<String[]> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("data/equipementt"));
		reader.setLinesToSkip(1); // Skip header line

		reader.setLineMapper(new LineMapper<String[]>() {
			@Override
			public @NonNull String[] mapLine(@NonNull String line, int lineNum) throws Exception {
				System.out.println("[v0] RAW LINE " + lineNum + ": " + line);
				
				if (line == null || line.trim().isEmpty()) {
					return new String[0];
				}
				
				String l = line.trim();
				if (l.length() >= 2 && l.startsWith("\"") && l.endsWith("\"")) {
					l = l.substring(1, l.length() - 1);
				}
				
				System.out.println("[v0] AFTER REMOVING OUTER QUOTES: " + l);
				
				List<String> tokens = new ArrayList<>();
				StringBuilder current = new StringBuilder();
				boolean inQuotes = false;
				
				for (int i = 0; i < l.length(); i++) {
					char c = l.charAt(i);
					
					if (c == '"') {
						// Check for doubled quotes ""
						if (i + 1 < l.length() && l.charAt(i + 1) == '"') {
							current.append('"');
							i++; // Skip next quote
						} else {
							// Toggle quote state
							inQuotes = !inQuotes;
						}
					} else if (c == ',' && !inQuotes) {
						// Field separator outside quotes
						String field = current.toString().trim();
						tokens.add(field);
						current = new StringBuilder();
					} else {
						current.append(c);
					}
				}
				// Add last field
				String field = current.toString().trim();
				tokens.add(field);
				
				String[] result = tokens.toArray(new String[0]);
				System.out.println("[v0] PARSED " + result.length + " FIELDS:");
				for (int i = 0; i < result.length; i++) {
					System.out.println("[v0]   [" + i + "] = '" + result[i] + "'");
				}
				
				return result;
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<String[], Cable> processor() {
		return new Processor();
	}

	@Bean
	public DBWriter dbWriter(CableRepository repository) {
		return new DBWriter(repository);
	}

	@Bean
	public Step importStep(DBWriter dbWriter) {
		return new org.springframework.batch.core.step.builder.StepBuilder("importStep", jobRepository)
			.<String[], Cable>chunk(50, transactionManager)
			.reader(reader())
			.processor(processor())
			.writer(dbWriter)
			.build();
	}

	@Bean
	public Job importJob(Step importStep) {
		return new org.springframework.batch.core.job.builder.JobBuilder("importJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(importStep)
			.build();
	}
}
