package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentsController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<?> payment(@RequestBody PaymentDTO paymentDTO){

        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        if(card == null){
            return new ResponseEntity<>("No se encontró tarjeta", HttpStatus.FORBIDDEN);
        }

        Client client = this.clientRepository.findByCards(card);
        if(client == null){
            return new ResponseEntity<>("No se encontró cliente", HttpStatus.FORBIDDEN);
        }
        if(paymentDTO.getThruDate().compareTo(LocalDate.now()) < 0){
            return new ResponseEntity<>("Datos incorrectos - Vencimiento", HttpStatus.FORBIDDEN);
        }
        if(!card.getCardHolder().equals(paymentDTO.getName())){
            return new ResponseEntity<>("Datos incorrectos - Nombre", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("Datos incorrectos - CVV",HttpStatus.FORBIDDEN);
        }

        List<Account> accounts = new ArrayList<>(client.getAccounts());
        Account account = accounts.stream().filter(a -> a.getBalance() >= paymentDTO.getAmount()).findAny().get();
        transactionRepository.save(new Transaction(paymentDTO.getAmount(), LocalDateTime.now(),  "Pago - " + paymentDTO.getDescription(), account, TransactionType.DEBITO));
        account.setBalance(account.getBalance() - paymentDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>("Pago procesado correctamente", HttpStatus.ACCEPTED);
    }
}
