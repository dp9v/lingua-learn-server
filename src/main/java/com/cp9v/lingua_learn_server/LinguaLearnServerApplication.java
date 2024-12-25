package com.cp9v.lingua_learn_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LinguaLearnServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinguaLearnServerApplication.class, args);
	}

}
