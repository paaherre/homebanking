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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
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
    /*@RequestMapping(value="/payment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)*/
    public ResponseEntity<?> payment(@RequestBody PaymentDTO paymentDTO) {

        /*
        axios.post("http://localhost:8080/api/payment", {
            name: "Melba Morel",
            number: "8387-3509-2174-4540",
            cvv: 823,
            thruDate: "2026-07-26",
            description: "Pago",
            amount: 12345
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))

        var url = 'http://localhost:8080/api/payment';
        var data = {name: "Melba Morel",
            number: "8387-3509-2174-4540",
            cvv: 823,
            thruDate: "2026-07-26",
            description: "Pago",
            amount: 12345};

        fetch(url, {
          method: 'POST', // or 'PUT'
          body: JSON.stringify(data), // data can be `string` or {object}!
          mode: 'no-cors',
          headers:{
            'Content-Type': 'application/json'
          }
        }).then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => console.log('Success:', response));

        */
    if(paymentDTO.getDescription().isEmpty() || paymentDTO.getAmount() < 1 ||paymentDTO.getCvv() == 0 || paymentDTO.getName().isEmpty() || paymentDTO.getNumber().isEmpty() || paymentDTO.getThruDate() == null){
        return new ResponseEntity<>("Datos incorrectos", HttpStatus.FORBIDDEN);
    }
        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        if(card == null){
            return new ResponseEntity<>("No se encontró tarjeta", HttpStatus.FORBIDDEN);
        }

        Client client = this.clientRepository.findByCards(card);
        if(client == null){
            return new ResponseEntity<>("No se encontró cliente", HttpStatus.FORBIDDEN);
        }
        if(paymentDTO.getThruDate().compareTo(LocalDate.now()) <= 0){
            return new ResponseEntity<>("Datos incorrectos - Vencimiento", HttpStatus.FORBIDDEN);
        }
        if(!card.getCardHolder().equalsIgnoreCase(paymentDTO.getName())){
            return new ResponseEntity<>("Datos incorrectos - Nombre", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("Datos incorrectos - CVV",HttpStatus.FORBIDDEN);
        }

        List<Account> accounts = new ArrayList<>(client.getAccounts());
        Account account = accounts.stream().filter(a -> a.getBalance() >= paymentDTO.getAmount()).findFirst().get();

        if(account.getNumber() == null){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
        }

        transactionRepository.save(new Transaction(- paymentDTO.getAmount(), LocalDateTime.now(),  "Pago - " + paymentDTO.getDescription(), account, TransactionType.DEBITO));
        account.setBalance(account.getBalance() - paymentDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>("Pago procesado correctamente", HttpStatus.ACCEPTED);
    }
}
