package com.equipement.config;

import com.equipement.batch.DBWriter;
import com.equipement.batch.Processor;
import com.equipement.entity.Cable;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// use JobBuilder/StepBuilder directly where needed
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.lang.NonNull;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;
import com.equipement.Repository.CableRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
	public FlatFileItemReader<Cable> reader() {
		FlatFileItemReader<Cable> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("data/equipement"));
		reader.setLinesToSkip(1);

		// Custom LineMapper: handle files where each record is wrapped in outer quotes and internal quotes are doubled
		reader.setLineMapper(new LineMapper<Cable>() {
			@Override
			public @NonNull Cable mapLine(@NonNull String line, int lineNum) throws Exception {
				if (line == null) return null;
				String l = line.trim();
				// remove surrounding wrapper quotes if present
				if (l.length() >= 2 && l.startsWith("\"") && l.endsWith("\"")) {
					l = l.substring(1, l.length() - 1);
				}
				// unescape doubled quotes
				l = l.replace("\"\"", "\"");
				// split on commas (after unwrapping outer quotes this should separate fields)
				String[] parts = l.split(",", -1);
				// normalize tokens: trim and remove surrounding quotes
				for (int i = 0; i < parts.length; i++) {
					if (parts[i] == null) continue;
					String s = parts[i].trim();
					if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length() - 1);
					s = s.trim();
					if (s.isEmpty()) parts[i] = null; else parts[i] = s;
				}

				Cable c = new Cable();
				c.setType(read(parts, 0));
				c.setSerialNb(read(parts, 1));
				c.setChannelNb(parseInteger(parts, 2));
				c.setLineName(read(parts, 3));
				c.setPointNb(parseDouble(parts, 4));
				c.setState(read(parts, 5));
				c.setAutoTest(read(parts, 6));
				c.setEasting(parseLong(parts, 7));
				c.setNorthing(parseLong(parts, 8));
				c.setElevation(parseInteger(parts, 9));
				c.setNoise(parseDouble(parts, 10));
				c.setDistortion(parseDouble(parts, 11));
				c.setPhase(parseDouble(parts, 12));
				c.setGain(parseDouble(parts, 13));
				c.setVersion(read(parts, 14));
				c.setLastTestDate(parseDate(parts, 15));
				c.setCxMaster(parseLong(parts, 16));

				if (c.getState() == null) c.setState("ko");
				return c;
			}
		});
		return reader;
	}

	// Helper parsing methods (copied from Processor for consistent behavior)
	private String read(String[] f, int idx) {
		if (f == null || idx >= f.length) return null;
		String s = f[idx];
		if (s == null) return null;
		s = s.trim();
		if (s.isEmpty()) return null;
		if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length() - 1);
		return s;
	}

	private Integer parseInteger(String[] f, int idx) {
		String s = read(f, idx);
		if (s == null) return null;
		try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
	}

	private Long parseLong(String[] f, int idx) {
		String s = read(f, idx);
		if (s == null) return null;
		try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
	}

	private Double parseDouble(String[] f, int idx) {
		String s = read(f, idx);
		if (s == null) return null;
		try { return Double.parseDouble(s); } catch (NumberFormatException e) { return null; }
	}

	private Date parseDate(String[] f, int idx) {
		String s = read(f, idx);
		if (s == null) return null;
		String t = s;
		int pos = t.indexOf(" UTC");
		if (pos > 0) t = t.substring(0, pos);
		String[] fmts = new String[]{"dd/MM/yyyy HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"};
		for (String fmt : fmts) {
			try { return new SimpleDateFormat(fmt).parse(t); } catch (ParseException ignored) { }
		}
		return null;
	}

	@Bean
	public ItemProcessor<Cable, Cable> processor() {
		// no transformation is required per your instructions — just return the object
		return item -> item;
	}

	@Bean
	public DBWriter dbWriter(CableRepository repository) {
		return new DBWriter(repository);
	}

	@Bean
	public Step importStep(DBWriter dbWriter) {
	return new org.springframework.batch.core.step.builder.StepBuilder("importStep", jobRepository)
		.<Cable, Cable>chunk(50, transactionManager)
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
