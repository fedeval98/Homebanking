package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // notacion para ejecutarlo apenas inicia la app
		public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
			return args -> {
				Client melba = new Client("Melba","Morel","melba@mindhub.com");
				clientRepository.save(melba);
				System.out.println(melba);

				Account VIN001 = new Account("VIN001", 5000, LocalDate.now());
				Account VIN002 = new Account("VIN002", 7500, LocalDate.now().plusDays(1));

				melba.addAccount(VIN001);
				melba.addAccount(VIN002);

				accountRepository.save(VIN001);
				accountRepository.save(VIN002);

				Client fede = new Client("Fede", "Val", "fedeval@gmail.com");
				clientRepository.save(fede);

				Account VIN003 = new Account("VIN003", 2000, LocalDate.now());
				Account VIN004 = new Account("VIN004", 9000, LocalDate.now().plusDays(2));
				fede.addAccount(VIN003);
				fede.addAccount(VIN004);
				accountRepository.save(VIN003);
				accountRepository.save(VIN004);


			}; //cierre args
		} // cierre initData
} // cierre de HomebankingApplication
