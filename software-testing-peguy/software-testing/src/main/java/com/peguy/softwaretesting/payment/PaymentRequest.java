package com.peguy.softwaretesting.payment;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PaymentRequest {
    private Payment payment;
}
