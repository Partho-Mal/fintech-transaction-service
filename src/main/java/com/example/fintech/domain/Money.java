package com.example.fintech.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Embeddable // <--- Required for JPA to embed this in Transaction
public class Money {

    private BigDecimal amount;
    private String currency;

    // 1. JPA requires a protected no-arg constructor
    protected Money() {
    }

    public Money(BigDecimal amount, String currency) {
        // 2. Value Objects should enforce validity on creation
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency must not be null");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // 3. Implement equals/hashCode for Value Object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}