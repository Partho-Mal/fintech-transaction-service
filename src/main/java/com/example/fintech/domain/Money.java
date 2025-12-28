//Money
//amount
//currency
//constructor
//getters

 package com.example.fintech.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Money {

    private BigDecimal amount;
    private String currency;

    public Money(BigDecimal amount, String currency){
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
