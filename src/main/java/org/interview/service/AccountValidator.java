package org.interview.service;

import org.interview.data.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class AccountValidator {

    private static final List<String> supportedCurrencies = List.of("USD", "EUR");

    static void validate(Account account) {
        if (account == null) {
            throw new IllegalStateException("The account must be set");
        }

        AtomicReference<BigDecimal> balance = account.getBalance();

        if (balance == null) {
            throw new IllegalStateException("The account balance must be set");
        }
        if (BigDecimal.ZERO.compareTo(balance.get()) >= 0) {
            throw new IllegalStateException("The balance must be positive; balance = " + balance.get());
        }

        if (balance.get().scale() > 2) {
            throw new IllegalStateException("The account balance scale must be less or equal 2");
        }

        if (account.getCurrency() == null) {
            throw new IllegalStateException("The account currency must be set");
        }

        if (!supportedCurrencies.contains(account.getCurrency())) {
            throw new IllegalStateException("The account currency doesn't supported. Valid currencies : "
                    + String.join(", ", supportedCurrencies));
        }
    }
}
