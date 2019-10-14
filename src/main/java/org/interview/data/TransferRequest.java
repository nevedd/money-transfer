package org.interview.data;

import lombok.*;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    protected Long sourceAccountId;
    protected Long destinationAccountId;
    protected BigDecimal amount;
    protected String currency;

}
