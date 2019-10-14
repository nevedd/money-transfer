package org.interview.data;

import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class Account {

    private Long id;
    private AtomicReference<BigDecimal> balance;
    private String currency;
}
