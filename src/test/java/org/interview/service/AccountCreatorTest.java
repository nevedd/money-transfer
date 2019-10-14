package org.interview.service;

import org.interview.data.Account;
import org.interview.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;


@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountValidator.class, AccountRepository.class})
public class AccountCreatorTest {

    @Test
    public void shouldValidatorAndRepositoryCalledOnce() throws Exception {

        PowerMockito.mockStatic(AccountValidator.class);
        PowerMockito.mockStatic(AccountRepository.class);

        Account account = new Account();
        account.setId(123L);
        account.setBalance(new AtomicReference<>(new BigDecimal("1000")));
        account.setCurrency("USD");


        PowerMockito.doNothing().when(AccountValidator.class, "validate", account);
        PowerMockito.doNothing().when(AccountRepository.class, "create", account);

        AccountCreator.create(account);

        PowerMockito.verifyStatic(AccountValidator.class);
        AccountValidator.validate(account);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.create(account);

    }
}
