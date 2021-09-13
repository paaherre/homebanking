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
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(CardRepository cardRepository, ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {



			Client client1 = clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("Password1")));
			Client client2 = clientRepository.save(new Client("Pablo", "Herrera", "paaherre@gmail.com", passwordEncoder.encode("Password2")));
			clientRepository.save(new Client("admin", "admin", "admin@admin.com",passwordEncoder.encode("admin")));


			Account accMelba1 = accountRepository.save(new Account("VIN001", LocalDateTime.now(), 5000.00, client1, AccountType.AHORRO));
			Account accMelba2 = accountRepository.save(new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, client1, AccountType.AHORRO));
			Account accPablo1 = accountRepository.save(new Account("VIN003", LocalDateTime.now(), 7500500.00, client2, AccountType.AHORRO));

			transactionRepository.save(new Transaction(1000.00, LocalDateTime.now(), "Account deposit", accMelba1, TransactionType.CREDITO));
			accMelba1.setBalance(accMelba1.getBalance() + 1000);
			accountRepository.save(accMelba1);
			transactionRepository.save(new Transaction(-1000.00, LocalDateTime.now(), "Account maintain", accMelba1, TransactionType.DEBITO));
			accMelba1.setBalance(accMelba1.getBalance() -1000);
			accountRepository.save(accMelba1);
			transactionRepository.save(new Transaction(-1250.00, LocalDateTime.now(), "Services payments", accMelba1, TransactionType.DEBITO));
			accMelba1.setBalance(accMelba1.getBalance() -1250);
			accountRepository.save(accMelba1);
			transactionRepository.save(new Transaction(500.00, LocalDateTime.now(), "Transfer received", accMelba1, TransactionType.CREDITO));
			accMelba1.setBalance(accMelba1.getBalance() + 500);
			accountRepository.save(accMelba1);
			transactionRepository.save(new Transaction(-2000.00, LocalDateTime.now(), "Card payment", accMelba2, TransactionType.DEBITO));
			accMelba2.setBalance(accMelba2.getBalance() -2000);
			accountRepository.save(accMelba2);
			transactionRepository.save(new Transaction(-900.00, LocalDateTime.now(), "Account maintain", accMelba2, TransactionType.DEBITO));
			accMelba2.setBalance(accMelba2.getBalance() -900);
			accountRepository.save(accMelba2);
			transactionRepository.save(new Transaction(-850.00, LocalDateTime.now(), "Services payments", accMelba2, TransactionType.DEBITO));
			accMelba2.setBalance(accMelba2.getBalance() - 850);
			accountRepository.save(accMelba2);
			transactionRepository.save(new Transaction(1000.00, LocalDateTime.now(), "Transfer received", accMelba2, TransactionType.CREDITO));
			accMelba2.setBalance(accMelba2.getBalance() + 1000);
			accountRepository.save(accMelba2);
			transactionRepository.save(new Transaction(-2000.00, LocalDateTime.now(), "Services payments", accPablo1, TransactionType.DEBITO));
			accPablo1.setBalance(accPablo1.getBalance() - 2000);
			accountRepository.save(accPablo1);
			transactionRepository.save(new Transaction(300000.00, LocalDateTime.now(), "Incoming", accPablo1, TransactionType.CREDITO));
			accPablo1.setBalance(accPablo1.getBalance() + 300000);
			accountRepository.save(accPablo1);

			Loan loan1 = loanRepository.save(new Loan("Mortgage", 500000, List.of(12,24,36,48,60),20));
			Loan loan2 = loanRepository.save(new Loan("Personal", 100000, List.of(6,12,24),30));
			Loan loan3 = loanRepository.save(new Loan("Auto", 300000, List.of(6,12,24,36),25));

			clientLoanRepository.save(new ClientLoan(400000, 60, client1,loan1, "VIN001" ));
			clientLoanRepository.save(new ClientLoan(50000, 12, client1, loan2, "VIN002"));
			clientLoanRepository.save(new ClientLoan(100000, 24, client1, loan2,"VIN001"));
			clientLoanRepository.save(new ClientLoan(200000, 36, client2, loan3,"VIN003"));

			cardRepository.save(new Card(client1, "3325-6745-7876-4445", 990, LocalDate.now(), LocalDate.now().plusYears(5), CardType.DEBITO, CardColor.GOLD));
			cardRepository.save(new Card(client1, "2234-6745-5522-7888", 750, LocalDate.now(), LocalDate.now().plusYears(5), CardType.CREDITO, CardColor.SILVER));
			cardRepository.save(new Card(client2, "4546-5169-7821-5597", 154, LocalDate.now(), LocalDate.now().plusYears(5), CardType.CREDITO, CardColor.TITANIUM));



		};
	}
}
