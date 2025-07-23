package com.ahphar.backend_quiz_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BackendQuizGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendQuizGameApplication.class, args);
	}

}
 