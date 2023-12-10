package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // notacion para ejecutarlo apenas inicia la app
		public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
			return args -> {
				Client melba = new Client("Melba","Morel","melba@mindhub.com");
				clientRepository.save(melba);
				System.out.println(melba);

				Account VIN001 = new Account("VIN001", 5000, LocalDate.now());
				Account VIN002 = new Account("VIN002", 7500, LocalDate.now().plusDays(1));

				Transaction TIN001 = new Transaction(VIN001, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Envio a Jorge");
				Transaction TIN002 = new Transaction(VIN002, LocalDateTime.now(),2000, TransactionType.CREDIT, "Deposito de Mabel");
				Transaction TIN003 = new Transaction(VIN001, LocalDateTime.now(),500, TransactionType.CREDIT, "Deposito de Lorena");
				Transaction TIN004 = new Transaction(VIN002, LocalDateTime.now(),-1000, TransactionType.DEBIT,"Pago a Steam");


				melba.addAccount(VIN001);
				melba.addAccount(VIN002);

				VIN001.addTransaction(TIN001);
				VIN001.addTransaction(TIN003);
				VIN002.addTransaction(TIN002);
				VIN002.addTransaction(TIN004);

				accountRepository.save(VIN001);
				accountRepository.save(VIN002);


				transactionRepository.save(TIN001);
				transactionRepository.save(TIN002);
				transactionRepository.save(TIN003);
				transactionRepository.save(TIN004);


				Client fede = new Client("Fede", "Val", "fedeval@gmail.com");
				clientRepository.save(fede);

				Account VIN003 = new Account("VIN003", 2000, LocalDate.now());
				Account VIN004 = new Account("VIN004", 9000, LocalDate.now().plusDays(2));

				Transaction TIN005 = new Transaction(VIN003, LocalDateTime.now(),100000, TransactionType.CREDIT,"Deposito de Lucas");
				Transaction TIN006 = new Transaction(VIN003, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Pago a Edenor");
				Transaction TIN007 = new Transaction(VIN004, LocalDateTime.now(),5000, TransactionType.CREDIT,"Deposito de Pedro");
				Transaction TIN008 = new Transaction(VIN004, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Pago a Epic Games");

				fede.addAccount(VIN003);
				fede.addAccount(VIN004);

				VIN003.addTransaction(TIN005);
				VIN003.addTransaction(TIN006);
				VIN004.addTransaction(TIN007);
				VIN004.addTransaction(TIN008);

				accountRepository.save(VIN003);
				accountRepository.save(VIN004);

				transactionRepository.save(TIN005);
				transactionRepository.save(TIN006);
				transactionRepository.save(TIN007);
				transactionRepository.save(TIN008);

			}; //cierre args
		} // cierre initData
} // cierre de HomebankingApplication
