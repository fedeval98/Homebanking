package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Bean // notacion para ejecutarlo apenas inicia la app
		public CommandLineRunner initData(ClientRepository clientRepository,
										  AccountRepository accountRepository,
										  TransactionRepository transactionRepository,
										  LoanRepository loanRepository,
										  ClientLoanRepository clientLoanRepository,
										  CardRepository cardRepository){
			return args -> {
				Client melba = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("melba123456"));
				melba.setRole(RoleType.ADMIN);
				clientRepository.save(melba);
				System.out.println(melba);

				Account VIN001 = new Account("VIN001", 5000, LocalDate.now());
				Account VIN002 = new Account("VIN002", 7500, LocalDate.now().plusDays(1));

				Transaction TIN001 = new Transaction(VIN001, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Transfer to Jorge");
				Transaction TIN002 = new Transaction(VIN002, LocalDateTime.now(),2000, TransactionType.CREDIT, "Deposit of Mabel");
				Transaction TIN003 = new Transaction(VIN001, LocalDateTime.now(),500, TransactionType.CREDIT, "Deposit of Lorena");
				Transaction TIN004 = new Transaction(VIN002, LocalDateTime.now(),-1000, TransactionType.DEBIT,"Payment to Steam");

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

				Client fede = new Client("Fede", "Val", "fedeval@gmail.com", passwordEncoder.encode("Fede123456!"));
				fede.setRole(RoleType.ADMIN);
				clientRepository.save(fede);

				Account VIN003 = new Account("VIN003", 2000, LocalDate.now());
				Account VIN004 = new Account("VIN004", 9000, LocalDate.now().plusDays(2));

				Transaction TIN005 = new Transaction(VIN003, LocalDateTime.now(),100000, TransactionType.CREDIT,"Deposit of Lucas");
				Transaction TIN006 = new Transaction(VIN003, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Payment to Edenor");
				Transaction TIN007 = new Transaction(VIN004, LocalDateTime.now(),5000, TransactionType.CREDIT,"Deposito of Pedro");
				Transaction TIN008 = new Transaction(VIN004, LocalDateTime.now(),-10000, TransactionType.DEBIT,"Payment to Epic Games");

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

				Loan Mortgage = new Loan("Mortgage",500000, Arrays.asList(12, 24, 36, 48, 60));
				Loan Personal = new Loan("Personal",100000,Arrays.asList(6,12,24));
				Loan Automotive = new Loan("Automotive", 300000,Arrays.asList(6,12,24,36));

				loanRepository.save(Mortgage);
				loanRepository.save(Personal);
				loanRepository.save(Automotive);

				ClientLoan melbaMortage = new ClientLoan(400000,60);
				ClientLoan melbaPersonal = new ClientLoan(50000,12);
				ClientLoan fedePersonal = new ClientLoan(100000,24);
				ClientLoan fedeAutomotive = new ClientLoan(200000,36);

				melba.addClientLoan(melbaMortage);
				Mortgage.ClientLoan(melbaMortage);

				melba.addClientLoan(melbaPersonal);
				Personal.ClientLoan(melbaPersonal);

				fede.addClientLoan(fedePersonal);
				Personal.ClientLoan(fedePersonal);

				fede.addClientLoan(fedeAutomotive);
				Automotive.ClientLoan(fedeAutomotive);

				clientLoanRepository.save(melbaMortage);
				clientLoanRepository.save(melbaPersonal);
				clientLoanRepository.save(fedePersonal);
				clientLoanRepository.save(fedeAutomotive);

				Card MelbaGold = new Card("Melba Morel",CardType.DEBIT,CardColor.GOLD,"4323-2342-1232-6958",345,LocalDate.now(),LocalDate.now().plusYears(5));
				Card MelbaTitanium = new Card("Melba Morel",CardType.CREDIT,CardColor.TITANIUM,"4323-2342-1232-2587",157,LocalDate.now(),LocalDate.now().plusYears(5));
				Card FedeSilver = new Card("Fede Val",CardType.CREDIT,CardColor.SILVER,"2587-4572-3541-5575",321,LocalDate.now(),LocalDate.now().plusYears(5));

				melba.addCard(MelbaGold);
				melba.addCard(MelbaTitanium);
				fede.addCard(FedeSilver);

				cardRepository.save(MelbaGold);
				cardRepository.save(MelbaTitanium);
				cardRepository.save(FedeSilver);


			}; //cierre args
		} // cierre initData
} // cierre de HomebankingApplication
