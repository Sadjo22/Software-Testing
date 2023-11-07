package com.peguy.softwaretesting.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRegistrationService {
    private final CustomerRegistrationRepo customerRegistrationRepo;

    @Autowired
    public CustomerRegistrationService(CustomerRegistrationRepo customerRegistrationRepo) {
        this.customerRegistrationRepo = customerRegistrationRepo;
    }

    public Optional<String> registerNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //control if the customer exist in the db
        //if he doesnt exit, then add a new customer in the db
        return Optional.empty();
    }
}
