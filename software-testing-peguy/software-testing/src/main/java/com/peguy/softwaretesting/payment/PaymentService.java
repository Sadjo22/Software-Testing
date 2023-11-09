package com.peguy.softwaretesting.payment;

import com.peguy.softwaretesting.customer.Customer;
import com.peguy.softwaretesting.customer.CustomerRegistrationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRegistrationRepo customerRepository;
    private final CardPaymentCharger cardPaymentCharger;

    private final List<Currency> currencies = List.of(Currency.GBP, Currency.EURO, Currency.USD);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          CustomerRegistrationRepo customerRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
        //1. Does customer exists if not throw
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(!optionalCustomer.isPresent()){
            throw new IllegalStateException("The customer doesn't exists in the Db");
        }

        //2. Do we support the currency if not throw
        Currency currency = paymentRequest.getPayment().getCurrency();
        if(!currencies.stream().anyMatch(cur -> cur.equals(currency))) {
            throw new IllegalStateException("The currency " + currency + " is not supported");
        }

        //3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.cardCharge(paymentRequest.getPayment().getSource(),
                                                                            paymentRequest.getPayment().getAmount(),
                                                                            paymentRequest.getPayment().getCurrency(),
                                                                            paymentRequest.getPayment().getDescription()
                                                                           );
        //4. If not debited throw
        if(!cardPaymentCharge.isCardDebited()) {
           throw new IllegalStateException("the card hasn't been debited");
        }

        //5. Insert payment
        paymentRequest.getPayment().setCustomerId(customerId);
        paymentRepository.save(paymentRequest.getPayment());
    }
}
