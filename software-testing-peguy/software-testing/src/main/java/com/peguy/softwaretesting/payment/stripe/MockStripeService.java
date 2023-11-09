package com.peguy.softwaretesting.payment.stripe;

import com.peguy.softwaretesting.payment.CardPaymentCharge;
import com.peguy.softwaretesting.payment.CardPaymentCharger;
import com.peguy.softwaretesting.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@ConditionalOnProperty(value = "stripe.enabled",
                        havingValue = "false")
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge cardCharge(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true);
    }
}
