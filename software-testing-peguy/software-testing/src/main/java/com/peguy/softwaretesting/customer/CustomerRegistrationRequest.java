package com.peguy.softwaretesting.customer;

import lombok.*;

@ToString
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CustomerRegistrationRequest {
    private final Customer customer;
}
