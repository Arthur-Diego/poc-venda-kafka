package com.poc.venda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// @EnableEurekaClient
public class PocVendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocVendaApplication.class, args);
	}

}
