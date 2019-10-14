package org.interview.service;

import lombok.extern.slf4j.Slf4j;
import org.interview.data.TransferRequest;
import org.interview.data.TransferTransaction;
import org.interview.repository.TransferTransactionRepository;

@Slf4j
public class TransferQueueProducer {

    public static void produce(TransferRequest request) {
        TransferValidator.validateTransferRequest(request);
        TransferTransaction transferTransaction = TransferTransactionRepository.createByTransferRequest(request);
        TransferQueue.QUEUE.add(transferTransaction);
        log.info("Transfer transaction has been registered. " +
                "transaction_id = " + transferTransaction.getId());
    }
}
