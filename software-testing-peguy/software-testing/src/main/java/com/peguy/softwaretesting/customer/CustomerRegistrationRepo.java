package com.peguy.softwaretesting.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRegistrationRepo extends JpaRepository<Customer, UUID> {
    @Query(value = "select c.phoneNumber, c.name from Customer c where c.phoneNumber = :phone_number")
    Optional<Customer> selectCustomerByPhoneNumber(@Param("phone_number") String phone_number);

}
