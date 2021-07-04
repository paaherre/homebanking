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
			Client admin = clientRepository.save(new Client("admin", "admin", "admin@admin.com",passwordEncoder.encode("admin")));
			Account accMelba1 = accountRepository.save(new Account("VIN001", LocalDateTime.now(), 5000.00, client1));
			Account accMelba2 = accountRepository.save(new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, client1));
			Account accPablo1 = accountRepository.save(new Account("VIN003", LocalDateTime.now(), 7500500.00, client2));
			transactionRepository.save(new Transaction(1000.00, LocalDateTime.now(), "Depósito en Cuenta", accMelba1, TransactionType.CREDITO));
			transactionRepository.save(new Transaction(1000.00, LocalDateTime.now(), "Mantenimiento de Cuenta", accMelba1, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(1250.00, LocalDateTime.now(), "Pago de Servicios", accMelba1, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(500.00, LocalDateTime.now(), "Transferencia recibida", accMelba1, TransactionType.CREDITO));
			transactionRepository.save(new Transaction(2000.00, LocalDateTime.now(), "Pago de Tarjeta", accMelba2, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(900.00, LocalDateTime.now(), "Mantenimiento de Cuenta", accMelba2, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(850.00, LocalDateTime.now(), "Pago de Servicios", accMelba2, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(1000.00, LocalDateTime.now(), "Transferencia recibida", accMelba2, TransactionType.CREDITO));
			transactionRepository.save(new Transaction(2000.00, LocalDateTime.now(), "Pago de Servicios", accPablo1, TransactionType.DEBITO));
			transactionRepository.save(new Transaction(300000.00, LocalDateTime.now(), "Acreditación de Haberes", accPablo1, TransactionType.CREDITO));
			Loan loan1 = loanRepository.save(new Loan("Hipotecario", 500000, List.of(12,24,36,48,60)));
			Loan loan2 = loanRepository.save(new Loan("Personal", 100000, List.of(6,12,24)));
			Loan loan3 = loanRepository.save(new Loan("Automotriz", 300000, List.of(6,12,24,36)));
			clientLoanRepository.save(new ClientLoan(400000, 60, client1,loan1));
			clientLoanRepository.save(new ClientLoan(50000, 12, client1, loan2));
			clientLoanRepository.save(new ClientLoan(100000, 24, client2, loan2));
			clientLoanRepository.save(new ClientLoan(200000, 36, client2, loan3));
			Card card1 = cardRepository.save(new Card(client1, "3325-6745-7876-4445", 990, LocalDate.now(), LocalDate.now().plusYears(5), CardType.DEBITO, CardColor.GOLD));
			Card card2 = cardRepository.save(new Card(client1, "2234-6745-5522-7888", 750, LocalDate.now(), LocalDate.now().plusYears(5), CardType.CREDITO, CardColor.SILVER));
			Card card3 = cardRepository.save(new Card(client2, "4546-5169-7821-5597", 154, LocalDate.now(), LocalDate.now().plusYears(5), CardType.CREDITO, CardColor.TITANIUM));
		};
	}
}
