package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // notacion para ejecutarlo apenas inicia la app
		public CommandLineRunner initData(ClientRepository clientRepository,
										  AccountRepository accountRepository,
										  TransactionRepository transactionRepository,
										  LoanRepository loanRepository,
										  ClientLoanRepository clientLoanRepository){
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

				Loan hipotecario = new Loan("Hipotecario",500000, Arrays.asList(12, 24, 36, 48, 60));
				Loan personal = new Loan("Personal",100000,Arrays.asList(6,12,24));
				Loan automotriz = new Loan("Automotriz", 300000,Arrays.asList(6,12,24,36));

				loanRepository.save(hipotecario);
				loanRepository.save(personal);
				loanRepository.save(automotriz);

				ClientLoan melbahipotecario = new ClientLoan(400000,60);
				ClientLoan melbapersonal = new ClientLoan(50000,12);
				ClientLoan fedepersonal = new ClientLoan(100000,24);
				ClientLoan fedeautomotriz = new ClientLoan(200000,36);

				melba.addClientLoan(melbahipotecario);
				hipotecario.ClientLoan(melbahipotecario);

				melba.addClientLoan(melbapersonal);
				personal.ClientLoan(melbapersonal);

				fede.addClientLoan(fedepersonal);
				personal.ClientLoan(fedepersonal);

				fede.addClientLoan(fedeautomotriz);
				automotriz.ClientLoan(fedeautomotriz);

				clientLoanRepository.save(melbahipotecario);
				clientLoanRepository.save(melbapersonal);
				clientLoanRepository.save(fedepersonal);
				clientLoanRepository.save(fedeautomotriz);

			}; //cierre args
		} // cierre initData
} // cierre de HomebankingApplication
