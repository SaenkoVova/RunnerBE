package com.vs.runner;

import com.vs.runner.util.services.FileService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunnerApplication implements CommandLineRunner {

	@Resource
	FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(RunnerApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		fileService.deleteAll();
		fileService.init();
	}
}
