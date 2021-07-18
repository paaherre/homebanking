package com.mindhub.homebanking.dtos;

import java.time.LocalDate;

public class PaymentDTO {

    private String name;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private String description;
    private double amount;

    public PaymentDTO() {
    }

    public PaymentDTO(String name, String number, int cvv, LocalDate thruDate, String description, double amount) {
        this.name = name;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.description = description;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
