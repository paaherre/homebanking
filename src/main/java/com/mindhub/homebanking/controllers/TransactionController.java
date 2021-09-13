package com.mindhub.homebanking.controllers;


import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.PDFExportDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.TransactionFilterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utility.Utility;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
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
    @PostMapping("/transfer/validation")
    public ResponseEntity<?> transferValidation(
            Authentication authentication, @RequestParam String toNumber
    ) {
        Account toAccount = accountRepository.findByNumber(toNumber);

        if (toAccount == null){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(toAccount.getClient().getFirstName() + " " + toAccount.getClient().getLastName(), HttpStatus.ACCEPTED);
    }

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
        transactionRepository.save(new Transaction(-amount, LocalDateTime.now(), "Transferencia Realizada - " + description, accountFrom, TransactionType.DEBITO));
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountRepository.save(accountFrom);
        transactionRepository.save(new Transaction(amount, LocalDateTime.now(), "Transferencia Recibida - " + description, accountTo, TransactionType.CREDITO));
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountRepository.save(accountTo);

        return new ResponseEntity<>("Se realizó la transferencia de manera exitosa", HttpStatus.ACCEPTED);
    }


    @PostMapping ("/transactions")
    public ResponseEntity <?> ListTransactionDTO(Authentication authentication, @RequestBody TransactionFilterDTO transactionFilterDTO){

        transactionFilterDTO.setHastaFecha(transactionFilterDTO.getHastaFecha().plusDays(1));

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(transactionFilterDTO.getAccountNumber());

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.FORBIDDEN);
        }

        List<LocalDate> listOfDates = transactionFilterDTO.getDesdeFecha().datesUntil(transactionFilterDTO.getHastaFecha()).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList1 = transactionDTOList.stream().filter(e -> listOfDates.contains(e.getTransactionDate().toLocalDate())).collect(Collectors.toList());
        transactionDTOList1.sort(Comparator.comparing(TransactionDTO::getTransactionDate).reversed());

        return new ResponseEntity<>(transactionDTOList1, HttpStatus.ACCEPTED);
    }

    @PostMapping ("/transaction/export/pdf")
    public void exportToPDF(HttpServletResponse response, Authentication authentication, @RequestBody TransactionFilterDTO transactionFilterDTO) throws DocumentException, IOException {

        transactionFilterDTO.setHastaFecha(transactionFilterDTO.getHastaFecha().plusDays(1));

        Account account = accountRepository.findByNumber(transactionFilterDTO.getAccountNumber());

        response.setContentType("aplication/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=file.pdf";
        response.setHeader(headerKey, headerValue);

        List<LocalDate> listOfDates = transactionFilterDTO.getDesdeFecha().datesUntil(transactionFilterDTO.getHastaFecha()).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList1 = transactionDTOList.stream().filter(e -> listOfDates.contains(e.getTransactionDate().toLocalDate())).collect(Collectors.toList());
        transactionDTOList1.sort(Comparator.comparing(TransactionDTO::getTransactionDate).reversed());
        PDFExportDTO exporter = new PDFExportDTO(transactionDTOList1);
        exporter.export(response);

    }
}
