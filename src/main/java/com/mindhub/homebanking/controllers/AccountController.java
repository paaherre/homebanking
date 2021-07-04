package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id){
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    //@RequestMapping(path= "/clients/current/accounts", method = RequestMethod.POST)
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        Client clientGeneral = clientRepository.findByEmail(authentication.getName());
        if (clientGeneral.getAccounts().size() > 2) {
            return new ResponseEntity<>("Already Have 3 accounts", HttpStatus.FORBIDDEN);
        };
        accountRepository.save(new Account("VIN" + Utility.getRandomNumber(0 , 99999999), LocalDateTime.now(), 0.00, clientGeneral));
        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }

}
