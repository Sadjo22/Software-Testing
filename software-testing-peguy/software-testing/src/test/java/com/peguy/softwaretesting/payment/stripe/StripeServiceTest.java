package com.peguy.softwaretesting.payment.stripe;

import com.peguy.softwaretesting.payment.CardPaymentCharge;
import com.peguy.softwaretesting.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.apache.coyote.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class StripeServiceTest {
    private StripeService stripeService;
    @Mock
    private StripeApi stripeApi;
    @Captor
    ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;
    @Captor
    ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stripeService = new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        //Given
        String cardSource = "MasterCard";
        BigDecimal amount = new BigDecimal(10.00);
        Currency currency = Currency.USD;
        String description = "hello";
        Charge charge = new Charge();
        charge.setPaid(true);
        given(stripeApi.create(anyMap(), any())).willReturn(charge);

        //When
        stripeService.cardCharge(cardSource, amount, currency, description);

        //Then
        then(stripeApi).should().create(mapArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());
        Map<String, Object> objectMap = mapArgumentCaptor.getValue();
        RequestOptions requestOptions = requestOptionsArgumentCaptor.getValue();

        assertThat(objectMap.get("source")).isEqualTo(cardSource);
        assertThat(objectMap.get("amount")).isEqualTo(amount);
        assertThat(objectMap.get("description")).isEqualTo(description);
        assertThat(objectMap.get("currency")).isEqualTo(currency);
        assertThat(objectMap.keySet().size()).isEqualTo(4);
    }

    @Test
    void itShouldNotChargeCard() throws StripeException {
        //Given
        String cardSource = "MasterCard";
        BigDecimal amount = new BigDecimal(10.00);
        Currency currency = Currency.USD;
        String description = "hello";
        Charge charge = new Charge();
        given(stripeApi.create(any(), any())).willReturn(charge);

        //When
        //stripeService.cardCharge(cardSource, amount, currency, description);

        //Then
        then(stripeApi).should(never()).create(anyMap(),any());

    }
}