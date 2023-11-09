package com.peguy.softwaretesting.customer;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
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
        String customerResponse = customerRegistrationService.registerNewCustomer(customerRegistrationRequest);
        if(customerResponse.equals("Ok")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Custom-Header", "foo");
            return new ResponseEntity<>("Inserted successfuly",headers, HttpStatus.OK);
        } else if (customerResponse.equals("Exist")) {
            return new ResponseEntity<>("Already present in the Db", HttpStatus.ALREADY_REPORTED);
        } else {
            return new ResponseEntity<>("Alrerady taken by another user", HttpStatus.BAD_REQUEST);
        }
    }


}
