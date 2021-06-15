package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = repository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			Client client2 = repository.save(new Client("Pablo", "Herrera", "paaherre@gmail.com"));
			accountRepository.save(new Account("VIN001", LocalDateTime.now(), 5000.00, client1));
			accountRepository.save(new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, client1));
			accountRepository.save(new Account("VIN003", LocalDateTime.now(), 7500500.00, client2));
		};
	}
}
