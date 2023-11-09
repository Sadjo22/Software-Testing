package com.peguy.softwaretesting.payment;

import com.peguy.softwaretesting.customer.Customer;
import com.peguy.softwaretesting.customer.CustomerRegistrationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;


class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CustomerRegistrationRepo customerRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;
    private PaymentService paymentService;
    @Captor
    ArgumentCaptor<Payment> argumentCaptor;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.paymentService = new PaymentService(paymentRepository, customerRepository, cardPaymentCharger);
    }

    @Test
    void itShouldChargeCard() {
        //Given
        UUID customerId = UUID.randomUUID();
        Long paymentId = 1L;
        String creditCard = "creditCard";
        Customer customer = new Customer(customerId, "Peguy", "3298711186");
        String paymentDoneSuccessfully = "paymentDoneSuccessfully";
        Payment payment  = new Payment(paymentId, customerId, new BigDecimal(10), Currency.GBP, creditCard, paymentDoneSuccessfully);
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        //When
        given(cardPaymentCharger.cardCharge(paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription())).willReturn(new CardPaymentCharge(true));

        paymentService.chargeCard(customerId, paymentRequest);

        //Then
        then(paymentRepository).should().save(argumentCaptor.capture());
        Payment paymentCaptor = argumentCaptor.getValue();
        assertThat(paymentCaptor).isEqualTo(payment);
    }

    @Test
    void itShouldNotChargedCardNotDebited() {
        //Given
        UUID customerId = UUID.randomUUID();
        Long paymentId = 1L;
        String creditCard = "creditCard";
        Customer customer = new Customer(customerId, "Peguy", "3298711186");
        String paymentDoneSuccessfully = "paymentDoneSuccessfully";
        Payment payment  = new Payment(paymentId, customerId, new BigDecimal(10), Currency.GBP, creditCard, paymentDoneSuccessfully);
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        //When
        given(cardPaymentCharger.cardCharge(paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription())).willReturn(new CardPaymentCharge(false));

        //Then
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("the card hasn't been debited");

        then(paymentRepository).should(never()).save(any(Payment.class));
        then(paymentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldNotChargedCardCurrencyNotSupported() {
        //Given
        UUID customerId = UUID.randomUUID();
        Long paymentId = 1L;
        String creditCard = "creditCard";
        Currency curr = Currency.FCFA;
        Customer customer = new Customer(customerId, "Peguy", "3298711186");
        String paymentDoneSuccessfully = "paymentDoneSuccessfully";
        Payment payment  = new Payment(paymentId, customerId, new BigDecimal(10), curr, creditCard, paymentDoneSuccessfully);
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        //When
        given(cardPaymentCharger.cardCharge(paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription())).willReturn(new CardPaymentCharge(true));

        //Then
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The currency " + curr + " is not supported");

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldNotChargedCardCustomerNotPresent() {
        //Given
        UUID customerId = UUID.randomUUID();

        //When customer not found
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The customer doesn't exists in the Db");

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}