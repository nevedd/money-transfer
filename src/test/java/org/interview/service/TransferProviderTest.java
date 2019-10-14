package org.interview.service;

import org.interview.data.Account;
import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;
import org.interview.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountRepository.class})
public class TransferProviderTest {

    @Test
    public void shouldProvideTransferSuccessfully() {
        PowerMockito.mockStatic(AccountRepository.class);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.45"));
        transferRequest.setCurrency("USD");


        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(System.nanoTime());
        transferTransaction.setStatus(TransferStatus.NEW);
        transferTransaction.setTransferRequest(transferRequest);
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));


        Account account1 = new Account();
        account1.setBalance(new AtomicReference<>(new BigDecimal("1000")));
        account1.setCurrency("USD");


        Mockito.when(AccountRepository.findById(transferRequest.getSourceAccountId()))
                .thenReturn(account1);

        Account account2 = new Account();
        account2.setCurrency("USD");
        account2.setBalance(new AtomicReference<>(new BigDecimal("2000")));
        Mockito.when(AccountRepository.findById(transferRequest.getDestinationAccountId()))
                .thenReturn(account2);

        TransferProvider.transfer(transferTransaction);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getSourceAccountId());

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getDestinationAccountId());

        Assert.assertEquals(new BigDecimal("876.55"), account1.getBalance().get());
        Assert.assertEquals(new BigDecimal("2123.45"), account2.getBalance().get());

    }
}
