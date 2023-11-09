package com.peguy.softwaretesting.payment.stripe;

import com.peguy.softwaretesting.payment.CardPaymentCharge;
import com.peguy.softwaretesting.payment.CardPaymentCharger;
import com.peguy.softwaretesting.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService implements CardPaymentCharger {

    private final StripeApi stripeApi;

    @Autowired
    public StripeService(StripeApi stripeApi) {
        this.stripeApi = stripeApi;
    }

    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
            .build();

    @Override
    public CardPaymentCharge cardCharge(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);
        try{
            Charge charge = stripeApi.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e){
            throw new IllegalStateException("Cannot make stripe charge");
        }
    }
}
