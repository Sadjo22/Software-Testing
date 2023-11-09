package com.peguy.softwaretesting.payment;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PaymentRequest {
    private final Payment payment;
}
