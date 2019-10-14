package org.interview.service;

import lombok.extern.slf4j.Slf4j;
import org.interview.data.Account;
import org.interview.data.TransferTransaction;
import org.interview.repository.AccountRepository;
import org.interview.data.TransferRequest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
class TransferProvider {

    static void transfer(TransferTransaction transferTransaction) {

        TransferRequest request = transferTransaction.getTransferRequest();

        BigDecimal amount = request.getAmount();

        while (true) {
            Account sourceAccount = AccountRepository.findById(request.getSourceAccountId());

            AtomicReference<BigDecimal> sourceAccountAmount = sourceAccount.getBalance();

            BigDecimal oldSourceAccountAmount = sourceAccountAmount.get();

            BigDecimal newSourceAccountAmount = oldSourceAccountAmount.subtract(amount);

            if (sourceAccountAmount.compareAndSet(oldSourceAccountAmount, newSourceAccountAmount)) {
                log.info("Money successfully withdrawn from source account. transaction_id = " + transferTransaction.getId());
                break;
            }

        }

        while (true) {
            Account destinationAccount = AccountRepository.findById(request.getDestinationAccountId());

            AtomicReference<BigDecimal> destinationAccountAmount = destinationAccount.getBalance();

            BigDecimal oldDestinationAccountAmount = destinationAccountAmount.get();

            BigDecimal newDestinationAccountAmount = oldDestinationAccountAmount.add(amount);

            if (destinationAccountAmount.compareAndSet(oldDestinationAccountAmount, newDestinationAccountAmount)) {
                log.info("Money successfully added to destination account. transaction_id = " + transferTransaction.getId());
                break;
            }
        }
    }
}
