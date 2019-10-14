package org.interview.service;

import org.interview.data.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(PowerMockRunner.class)
public class AccountValidatorTest {


    @Test
    public void shouldValidateOkWhenAccountIsValid() {

        Account account = new Account();
        account.setId(123L);
        account.setBalance(new AtomicReference<>(new BigDecimal("133.99")));
        account.setCurrency("USD");

        AccountValidator.validate(account);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenCurrencyIsNotSupported() {

        Account account = new Account();
        account.setId(123L);
        account.setBalance(new AtomicReference<>(new BigDecimal("133.99")));
        account.setCurrency("RUB");

        AccountValidator.validate(account);
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenBalanceIsNegative() {
        Account account = new Account();
        account.setId(123L);
        account.setBalance(new AtomicReference<>(new BigDecimal("-133.99")));
        account.setCurrency("EUR");

        AccountValidator.validate(account);
    }
}
