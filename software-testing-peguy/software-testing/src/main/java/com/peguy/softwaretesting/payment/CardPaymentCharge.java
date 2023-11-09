package com.peguy.softwaretesting.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@AllArgsConstructor
@ToString
@Service
@Getter
@Setter
public class CardPaymentCharge {
    private final boolean isCardDebited;

    public boolean isCardDebited() {
        return isCardDebited;
    }
}
