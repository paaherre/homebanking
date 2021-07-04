package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utility.Utility;
import org.hibernate.annotations.common.reflection.XMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

        @Autowired
    private ClientRepository clientRepository;

        @Autowired
        private CardRepository cardRepository;

        @PostMapping("/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color){

            Client clientGeneral = clientRepository.findByEmail(authentication.getName());

            if( clientGeneral.getCards().stream().filter(e -> e.getType().toString().equals(type.toString()) ).count() > 2) {
                return new ResponseEntity<>("403 prohibida", HttpStatus.FORBIDDEN);
            }
            cardRepository.save(new Card(clientGeneral, Utility.getCardNumber(1000,9999), Utility.getRandomNumber(100,999), LocalDate.now(), LocalDate.now().plusYears(5), type, color));
            return new ResponseEntity<>("201 Tarjeta Creada", HttpStatus.CREATED);
        }
}
