package org.interview.service;


import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;
import org.interview.repository.TransferTransactionRepository;
import org.junit.Assert;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({TransferValidator.class,
        TransferTransactionRepository.class})
public class TransferQueueProducerTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(TransferValidator.class);
        PowerMockito.mockStatic(TransferTransactionRepository.class);
    }

    @Test
    public void shouldQueueContainsOnly1TransactionWhenTransferRequestRegisterCorrectly() throws Exception {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(1L);
        transferRequest.setDestinationAccountId(2L);
        transferRequest.setAmount(new BigDecimal("123.45"));
        transferRequest.setCurrency("USD");


        TransferTransaction transaction = new TransferTransaction();
        transaction.setId(System.nanoTime());
        transaction.setStatus(TransferStatus.NEW);
        transaction.setTransferRequest(transferRequest);
        transaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));


        PowerMockito.doNothing().when(TransferValidator.class, "validateTransferRequest", transferRequest);


        Mockito.when(TransferTransactionRepository.createByTransferRequest(transferRequest))
                .thenReturn(transaction);

        TransferQueueProducer.produce(transferRequest);

        PowerMockito.verifyStatic(TransferValidator.class);
        TransferValidator.validateTransferRequest(transferRequest);

        PowerMockito.verifyStatic(TransferTransactionRepository.class);
        TransferTransactionRepository.createByTransferRequest(transferRequest);

        Assert.assertEquals(1, TransferQueue.QUEUE.size());

        Assert.assertEquals(transaction, TransferQueue.QUEUE.poll());

    }
}
