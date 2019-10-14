package org.interview.service;


import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TransferValidator.class, TransferProvider.class})
public class TransferQueueConsumerTest {


    @Before
    public void setUp() {
        PowerMockito.mockStatic(TransferValidator.class);
        PowerMockito.mockStatic(TransferProvider.class);
    }


    @Test
    public void shouldPollTransactionFromQueueAndProcessCorrectlyWhenValidationIsOk() throws Exception {


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

        TransferQueue.QUEUE.put(transferTransaction);

        PowerMockito.doNothing().when(TransferValidator.class, "validateTransferTransaction", transferTransaction);
        PowerMockito.doNothing().when(TransferProvider.class, "transfer", transferTransaction);

        new Thread(() -> new TransferQueueConsumer().run()).start();

        PowerMockito.verifyStatic(TransferValidator.class);
        TransferValidator.validateTransferTransaction(transferTransaction);

        PowerMockito.verifyStatic(TransferProvider.class);
        TransferProvider.transfer(transferTransaction);

        Assert.assertEquals(0, TransferQueue.QUEUE.size());
        Assert.assertEquals(TransferStatus.DONE, transferTransaction.getStatus());


    }

}
