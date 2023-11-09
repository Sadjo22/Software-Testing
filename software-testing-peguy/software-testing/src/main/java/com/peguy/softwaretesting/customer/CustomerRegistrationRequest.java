package com.peguy.softwaretesting.customer;

import lombok.*;

@ToString
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationRequest {
    private Customer customer;
}
