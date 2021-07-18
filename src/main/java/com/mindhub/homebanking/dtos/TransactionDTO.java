package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Double amount;
    private LocalDateTime transactionDate;
    private String description;
    private TransactionType type;
    private double balance;


    public TransactionDTO (Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
        this.type = transaction.getType();
        this.balance = transaction.getBalance();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
