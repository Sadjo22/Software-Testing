package com.peguy.softwaretesting.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRegistrationRepo customerRegistrationRepo;
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService customerRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRegistrationService = new CustomerRegistrationService(customerRegistrationRepo);
    }

    @Test
    void itShouldSaveNewCustomer() {
        //Given
        UUID customerId = UUID.randomUUID();
        String phone_number ="3298711186";
        Customer customer = new Customer(customerId, "Peguy", phone_number);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);
        given(customerRegistrationRepo.selectCustomerByPhoneNumber(phone_number)).willReturn(Optional.empty());

        //When
        customerRegistrationService.registerNewCustomer(customerRegistrationRequest);

        //Then
        then(customerRegistrationRepo).should().save(customerArgumentCaptor.capture());
        Customer customer1 = customerArgumentCaptor.getValue();
        assertThat(customer1).isEqualTo(customer);
        assertThat(customer1.getName()).isEqualTo("Peguy");
        assertThat(customer1.getPhoneNumber()).isEqualTo(phone_number);


    }
}