package com.equipement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class EquipementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquipementApplication.class, args);
	}

	// No auto-run of the batch job at startup. Use a REST endpoint to trigger the job manually.

}
