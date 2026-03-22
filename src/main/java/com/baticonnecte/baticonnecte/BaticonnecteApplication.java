package com.baticonnecte.baticonnecte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaticonnecteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaticonnecteApplication.class, args);
		System.out.println("🚀 Serveur connecté avec succès sur le port 8090");
	}

}
