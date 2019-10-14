package org.interview.service;

import lombok.extern.slf4j.Slf4j;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TransferQueueConsumer {

    public void run() {
        while (true) {
            TransferTransaction transferTransaction = null;
            try {
                transferTransaction = TransferQueue.QUEUE.poll(365, TimeUnit.DAYS);
                if (transferTransaction == null) {
                    continue;
                }
                log.info("Processing of transfer transaction. transaction_id = " + transferTransaction.getId());
                TransferValidator.validateTransferTransaction(transferTransaction);

                transferTransaction.setStatus(TransferStatus.PROCESSING);
                transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));

                TransferProvider.transfer(transferTransaction);

                transferTransaction.setStatus(TransferStatus.DONE);
                transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));

                log.info("Money successfully transferred. transaction_id = " + transferTransaction.getId());

            } catch (InterruptedException e) {
                log.error("InterruptedException occurred while processing transfer transactions", e);
            } catch (Exception e) {
                if (transferTransaction != null) {
                    log.error("Exception occurred while processing transfer transaction. transaction_id = "
                            + transferTransaction.getId(), e);

                    transferTransaction.setStatus(TransferStatus.FAILED);
                    transferTransaction.setErrorMessage(e.getMessage());
                    transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));

                } else {
                    log.error("Exception occurred while processing transfer transactions", e);
                }
            }
        }
    }

}
