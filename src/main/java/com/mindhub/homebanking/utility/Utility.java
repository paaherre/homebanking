package com.mindhub.homebanking.utility;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;


public class Utility {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static String getCardNumber (int min, int max){
        return getRandomNumber(min, max) + "-" + getRandomNumber(min, max) + "-" + getRandomNumber(min, max) + "-" + getRandomNumber(min, max);
    }
    public static double calcularBalance (Set<Transaction> transactions , Account accountOrigin) {
        double balance = accountOrigin.getBalance();
        for (Transaction transaction : transactions) {
            balance = balance + transaction.getAmount();
        }
        return balance;
    }

}
