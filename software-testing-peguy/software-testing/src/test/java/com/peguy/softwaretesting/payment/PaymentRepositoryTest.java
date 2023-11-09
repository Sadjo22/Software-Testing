package com.peguy.softwaretesting.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentRepositoryTest(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Test
    void itShouldInsertPayment() {
        //Given
        String sold = "sold";
        long paymentId = 1L;
        String creditCard = "creditCard";
        Payment payment = new Payment(paymentId, UUID.randomUUID(),new BigDecimal(10),Currency.USD,creditCard, sold);

        //When
        paymentRepository.save(payment);

        //Then
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        assertThat(optionalPayment).isPresent();
        assertThat(optionalPayment).hasValue(payment);

    }
}