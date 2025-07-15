package com.gallego.curso.springboot.beerbackend.backend_beers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BackendBeersApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendBeersApplication.class, args);
	}

}
