package com.peguy.softwaretesting.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRegistrationRepoTest {

     private CustomerRegistrationRepo customerRegistrationRepo;

    @Autowired
    public CustomerRegistrationRepoTest(CustomerRegistrationRepo customerRegistrationRepo) {
        this.customerRegistrationRepo = customerRegistrationRepo;
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "3298711186";
        Customer customer = new Customer(id, "Peguy", phoneNumber);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        //When
        customerRegistrationRepo.save(customer);
        //Then
        Optional<Customer> optionalCustomer = customerRegistrationRepo.findById(id);
        assertThat(optionalCustomer).isPresent();
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> c.equals(customer));
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(c -> {
            c.getPhoneNumber().equals(phoneNumber);
            c.getId().equals(id);
            c.getName().equals("Peguy");
        });

    }
}