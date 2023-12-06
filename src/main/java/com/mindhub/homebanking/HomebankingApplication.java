package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // notacion para ejecutarlo apenas inicia la app
		public CommandLineRunner initData(ClientRepository clientRepository){
			return args -> {
				Client fede = new Client("Melba","Morel","melba@mindhub.com");
				System.out.println(fede);

				clientRepository.save(fede);
				System.out.println(fede);

			}; //cierre args
		} // cierre initData
} // cierre de HomebankingApplication
