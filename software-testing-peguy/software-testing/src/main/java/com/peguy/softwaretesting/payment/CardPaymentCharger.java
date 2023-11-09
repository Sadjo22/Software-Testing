package com.peguy.softwaretesting.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public interface CardPaymentCharger {
    public CardPaymentCharge cardCharge(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    );
}
