package com.peguy.softwaretesting.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class CustomerRegistrationRepoTest {

     private final CustomerRegistrationRepo customerRegistrationRepo;

    @Autowired
    public CustomerRegistrationRepoTest(CustomerRegistrationRepo customerRegistrationRepo) {
        this.customerRegistrationRepo = customerRegistrationRepo;
    }

    @Test
    void itShouldSaveCustomerByPhoneNumber() {
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

    @Test
    void itShouldNotSaveCustomerWhenTheNameIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "3298711186";
        Customer customer = new Customer(id, null, phoneNumber);

        //When && Then
        assertThatThrownBy(() -> customerRegistrationRepo.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value : com.peguy.softwaretesting.customer.Customer.name");
    }

    @Test
    void itShouldNotSaveCustomerWhenThePhoneNumberIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        String phoneNumber = null;
        Customer customer = new Customer(id, "Sadjo", phoneNumber);

        //When && Then
        assertThatThrownBy(() -> customerRegistrationRepo.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value : com.peguy.softwaretesting.customer.Customer.phoneNumber");
    }

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        UUID id = UUID.randomUUID();
        String phone = "3298711186";
        Customer customer = new Customer(id, "Sadjo", phone);

        //When
        customerRegistrationRepo.save(customer);
        Optional<Customer> optionalCustomer = customerRegistrationRepo.selectCustomerByPhoneNumber(phone);

        //Then
        assertThat(optionalCustomer).isPresent();
        assertThat(optionalCustomer.get().getPhoneNumber()).isEqualTo(phone);

    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumber() {
        //Given
        String phone = "3298711186";

        //When
        Optional<Customer> optionalCustomer = customerRegistrationRepo.selectCustomerByPhoneNumber(phone);

        //Then
        assertThat(optionalCustomer).isNotPresent();
    }

}