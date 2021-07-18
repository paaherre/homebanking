package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utility.Utility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
        assertThat(loans, hasItem(hasProperty("name", is("Hipotecario"))));
        assertThat(loans, hasItem(hasProperty("name", is("Automotriz"))));
    }

    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }
    @Test
    public void validateAccountAttributes(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("id", is(not(empty())))));
        assertThat(accounts, hasItem(hasProperty("number", is(not(empty())))));
        assertThat(accounts, hasItem(hasProperty("creationDate", is(not(empty())))));
        assertThat(accounts, hasItem(hasProperty("balance", is(not(empty())))));
        assertThat(accounts, hasItem(hasProperty("transactions", is(not(empty())))));
    }

    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }
    @Test
    public void validateCardAttributes(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("id", is(not(empty())))));
        assertThat(cards, hasItem(hasProperty("cardHolder", is(not(empty())))));
        assertThat(cards, hasItem(hasProperty("number", is(not(empty())))));
        assertThat(cards, hasItem(hasProperty("cvv", is(not(empty())))));
        assertThat(cards, hasItem(hasProperty("fromDate", is(not(empty())))));
    }

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }
    @Test
    public void validateClientAttributes(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("id", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("firstName", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("lastName", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("email", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("password", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("accounts", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("loans", is(not(empty())))));
        assertThat(clients, hasItem(hasProperty("cards", is(not(empty())))));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }
    @Test
    public void validateTransactionAttributes(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("id", is(not(empty())))));
        assertThat(transactions, hasItem(hasProperty("amount", is(not(empty())))));
        assertThat(transactions, hasItem(hasProperty("transactionDate", is(not(empty())))));
        assertThat(transactions, hasItem(hasProperty("description", is(not(empty())))));
        assertThat(transactions, hasItem(hasProperty("type", is(not(empty())))));
    }

    @Test
    public void cardNumbersCreated(){
        String cardNumber = Utility.getCardNumber(1000, 9000);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

}