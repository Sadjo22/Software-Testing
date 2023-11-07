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

    public String registerNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //control if the phone number is taken
        String phoneNumber = customerRegistrationRequest.getCustomer().getPhoneNumber();
        Optional<Customer> optionalCustomer = customerRegistrationRepo.selectCustomerByPhoneNumber(phoneNumber);

        if(optionalCustomer.isPresent()) {
            if(optionalCustomer.get().equals(customerRegistrationRequest.getCustomer())){
                System.out.println( "The phone number " + optionalCustomer.get().getPhoneNumber() + " belongs to: " + optionalCustomer.get().getName());
                return "Exist";
            } else {
                //throw new IllegalStateException("the phone number is already occupied");
                System.out.println("the phone number is already taken");
                return "Taken";
            }
        } else {
            customerRegistrationRepo.save(customerRegistrationRequest.getCustomer());
            return "Ok";
        }
    }
}
