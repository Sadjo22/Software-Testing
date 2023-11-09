package com.peguy.softwaretesting.payment;

import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@ToString
@AllArgsConstructor
@Getter
@Setter
public class CardPaymentCharge {

    private final boolean isCardDebited;

    public boolean isCardDebited() {
        return isCardDebited;
    }
}

//@AllArgsConstructor


