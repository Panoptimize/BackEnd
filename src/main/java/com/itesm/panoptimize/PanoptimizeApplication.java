package com.itesm.panoptimize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PanoptimizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanoptimizeApplication.class, args);
	}

}
