package com.peguy.softwaretesting.customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerRegistrationController {

    private CustomerRegistrationService customerRegistrationService;

    public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
    }

    @PutMapping
    public ResponseEntity<String> registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
