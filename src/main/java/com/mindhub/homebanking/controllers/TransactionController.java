package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Transactional
    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(
            Authentication authentication,
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String fromNumber, @RequestParam String toNumber
            ){

        Client clientGeneral = clientRepository.findByEmail(authentication.getName());
        Account accountFrom = accountRepository.findByNumber(fromNumber);
        Account accountTo = accountRepository.findByNumber(toNumber);

        // que los parámetros no estén vacíos
        if (amount == 0) {
           return  new ResponseEntity<>("amount mal", HttpStatus.FORBIDDEN);
        }
        if ( description.isEmpty()) {
            return new ResponseEntity<>("desc vacia", HttpStatus.FORBIDDEN);
        }
        if (fromNumber.isEmpty()){
            return new ResponseEntity<>("desc FromNumber", HttpStatus.FORBIDDEN);
        }
        if (toNumber.isEmpty()){
            return new ResponseEntity<>("desc toNumber", HttpStatus.FORBIDDEN);
        }
        // que los números de cuenta no sean iguales


        if (fromNumber.equals("toNumber")){
            return  new ResponseEntity<>("No se puede transferir a la misma cuenta", HttpStatus.FORBIDDEN);
        }
        // que exista la cuenta de origen
        if (accountRepository.findByNumber(fromNumber) == null) {
            return  new ResponseEntity<>("Cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
        // que la cuenta de origen pertenezca al cliente autenticado
        if (!clientGeneral.getAccounts().contains(accountFrom)) {
            return new ResponseEntity<>("que la cuenta de origen pertenezca al cliente autenticado", HttpStatus.FORBIDDEN);
        }
        // que exista la cuenta destino
        if (accountRepository.findByNumber(toNumber) == null){
            return new ResponseEntity<>("que exista la cuenta destino", HttpStatus.FORBIDDEN);
        }
        //que la cuenta de origen tenga el monto disponible
        if (Utility.calcularBalance(accountFrom.getTransactions(), accountFrom) < amount){
            return new ResponseEntity<>("que la cuenta de origen tenga el monto disponible", HttpStatus.FORBIDDEN);
        }
        // transacciones "DEBIT" y "CREDIT" para cta origen y cta destino
        transactionRepository.save(new Transaction(-amount, LocalDateTime.now(), "Pago de Servicios", accountFrom, TransactionType.DEBITO));
        transactionRepository.save(new Transaction(amount, LocalDateTime.now(), "Pago de Servicios", accountTo, TransactionType.CREDITO));
        return new ResponseEntity<>("Se realizó la transferencia de manera exitosa", HttpStatus.ACCEPTED);
    }

}
