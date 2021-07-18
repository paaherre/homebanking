package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loans (
            Authentication authentication,
            @RequestBody LoanAplicationDTO loanAplicationDTO
    ){
        Loan loan = this.loanRepository.findByName(loanAplicationDTO.getName());
        Account account = this.accountRepository.findByNumber(loanAplicationDTO.getAccount());
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (loanAplicationDTO.getAccount().isEmpty() || loanAplicationDTO.getName().isEmpty() || loanAplicationDTO.getAmount() == 0 || loanAplicationDTO.getPayment() == 0){
            return new ResponseEntity<>("Uno de los datos está vacío", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("No existe el Préstamo", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Monto solicitado super el permitido", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanAplicationDTO.getPayment())){
            return new ResponseEntity<>("Cantidad de cuotas incorrectas", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("Cuenta destino incorrecta", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Cuenta destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        clientLoanRepository.save(new ClientLoan(loanAplicationDTO.getAmount(), loanAplicationDTO.getPayment(), client,loan, account.getNumber() ));
        transactionRepository.save(new Transaction(loanAplicationDTO.getAmount(), LocalDateTime.now(),  "Préstamo - " + loan.getName(), account, TransactionType.CREDITO));
        account.setBalance(account.getBalance() + loanAplicationDTO.getAmount());
        accountRepository.save(account);
        return new ResponseEntity<>("Préstamo acreditado en la cuenta", HttpStatus.ACCEPTED);
    }

    @GetMapping("/loans")
    public List<LoanDTO> getLoans (){
        return this.loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/admin/loan")
    public ResponseEntity<?> createLoan(Authentication authentication, @RequestBody LoanDTO loanDTO){
        if (loanDTO.getInterest() == 0 || loanDTO.getMaxAmount() == 0 || loanDTO.getName().isEmpty() || loanDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Datos necesarios incompletos", HttpStatus.FORBIDDEN);
        }
        loanRepository.save(new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(),loanDTO.getInterest()));
        return new ResponseEntity<>("Prestamo creado correctamente", HttpStatus.CREATED);
    }
}
// LoanApplicationDTO - nro Cuenta - idLoan - amount - payment
// loanDTO
// loanRepository
//