package com.mindhub.homebanking.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionFilterDTO {
    private LocalDate desdeFecha;
    private LocalDate hastaFecha;
    private String accountNumber;

    public TransactionFilterDTO() {
    }

    public TransactionFilterDTO(LocalDate desdeFecha, LocalDate hastaFecha, String accountNumber) {
        this.desdeFecha = desdeFecha;
        this.hastaFecha = hastaFecha;
        this.accountNumber = accountNumber;
    }

    public LocalDate getDesdeFecha() {
        return desdeFecha;
    }

    public void setDesdeFecha(LocalDate desdeFecha) {
        this.desdeFecha = desdeFecha;
    }

    public LocalDate getHastaFecha() {
        return hastaFecha;
    }

    public void setHastaFecha(LocalDate hastaFecha) {
        this.hastaFecha = hastaFecha;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
