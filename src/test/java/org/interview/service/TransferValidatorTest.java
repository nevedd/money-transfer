package org.interview.service;

import org.interview.data.Account;
import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;
import org.interview.repository.AccountRepository;
import org.interview.repository.TransferTransactionRepository;
import org.junit.Before;
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
@PrepareForTest({AccountRepository.class, TransferTransactionRepository.class})
public class TransferValidatorTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AccountRepository.class);
        PowerMockito.mockStatic(TransferTransactionRepository.class);
    }

    @Test
    public void shouldValidateOkWhenTransferTransactionIsValid() {

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

        Mockito.when(TransferTransactionRepository.findById(transferTransaction.getId())).thenReturn(transferTransaction);

        Account mockAccount1 = new Account();
        mockAccount1.setBalance(new AtomicReference<>(new BigDecimal("1000")));
        mockAccount1.setCurrency("USD");


        Mockito.when(AccountRepository.findById(transferRequest.getSourceAccountId()))
                .thenReturn(mockAccount1);

        Account mockAccount2 = new Account();
        mockAccount2.setCurrency("USD");
        mockAccount2.setBalance(new AtomicReference<>(new BigDecimal("2000")));

        Mockito.when(AccountRepository.findById(transferRequest.getDestinationAccountId()))
                .thenReturn(mockAccount2);


        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getSourceAccountId());

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getDestinationAccountId());

        PowerMockito.verifyStatic(TransferTransactionRepository.class);
        TransferTransactionRepository.findById(transferTransaction.getId());

    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSourceAccountDoesNotExist() {

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.45"));
        transferRequest.setCurrency("USD");


        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(1111L);
        transferTransaction.setStatus(TransferStatus.NEW);
        transferTransaction.setTransferRequest(transferRequest);
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));


        Mockito.when(AccountRepository.findById(transferRequest.getSourceAccountId()))
                .thenReturn(null);

        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getSourceAccountId());

        PowerMockito.verifyZeroInteractions(TransferTransactionRepository.class);

    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenAccountCurrenciesAreDifferent() {

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.45"));
        transferRequest.setCurrency("USD");


        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(1111L);
        transferTransaction.setStatus(TransferStatus.NEW);
        transferTransaction.setTransferRequest(transferRequest);
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));


        Account mockAccount1 = new Account();
        mockAccount1.setBalance(new AtomicReference<>(new BigDecimal("1000")));
        mockAccount1.setCurrency("USD");


        Mockito.when(AccountRepository.findById(transferRequest.getSourceAccountId()))
                .thenReturn(mockAccount1);

        Account mockAccount2 = new Account();
        mockAccount2.setCurrency("EUR");
        mockAccount2.setBalance(new AtomicReference<>(new BigDecimal("2000")));

        Mockito.when(AccountRepository.findById(transferRequest.getDestinationAccountId()))
                .thenReturn(mockAccount2);

        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getSourceAccountId());

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getDestinationAccountId());

        PowerMockito.verifyZeroInteractions(TransferTransactionRepository.class);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenTransferTransactionStatusIsNotNEW() {

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.45"));
        transferRequest.setCurrency("USD");


        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(1111L);
        transferTransaction.setStatus(TransferStatus.PROCESSING);
        transferTransaction.setTransferRequest(transferRequest);
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));

        Mockito.when(TransferTransactionRepository.findById(transferTransaction.getId())).thenReturn(transferTransaction);

        Account mockAccount1 = new Account();
        mockAccount1.setBalance(new AtomicReference<>(new BigDecimal("1000")));
        mockAccount1.setCurrency("USD");

        Mockito.when(AccountRepository.findById(transferRequest.getSourceAccountId()))
                .thenReturn(mockAccount1);

        Account mockAccount2 = new Account();
        mockAccount2.setCurrency("USD");
        mockAccount2.setBalance(new AtomicReference<>(new BigDecimal("2000")));

        Mockito.when(AccountRepository.findById(transferRequest.getDestinationAccountId()))
                .thenReturn(mockAccount2);

        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getSourceAccountId());

        PowerMockito.verifyStatic(AccountRepository.class);
        AccountRepository.findById(transferRequest.getDestinationAccountId());

        PowerMockito.verifyStatic(TransferTransactionRepository.class);
        TransferTransactionRepository.findById(transferTransaction.getId());

    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenTransferRequestAmountScaleIs3() {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.453"));
        transferRequest.setCurrency("USD");


        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(1111L);
        transferTransaction.setStatus(TransferStatus.PROCESSING);
        transferTransaction.setTransferRequest(transferRequest);
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));

        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyZeroInteractions(AccountRepository.class);
        PowerMockito.verifyZeroInteractions(TransferTransactionRepository.class);
    }

}
