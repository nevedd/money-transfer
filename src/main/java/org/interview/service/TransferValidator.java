package org.interview.service;

import org.interview.data.Account;
import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;
import org.interview.repository.AccountRepository;
import org.interview.repository.TransferTransactionRepository;

import java.math.BigDecimal;

class TransferValidator {

    private static final BigDecimal TRANSACTION_MAX_AMOUNT = new BigDecimal(1_000_000);

    static void validateTransferTransaction(TransferTransaction transferTransaction) {
        validateTransferRequest(transferTransaction.getTransferRequest());

        if (TransferTransactionRepository.findById(transferTransaction.getId()).getStatus()
                != TransferStatus.NEW) {
            throw new IllegalStateException("Transfer status is not NEW");
        }
    }

    static void validateTransferRequest(TransferRequest request) {
        if (request == null) {
            throw new IllegalStateException("Transfer request is null");
        }

        Account sourceAccount = AccountRepository.findById(request.getSourceAccountId());
        if (sourceAccount == null) {
            throw new IllegalStateException("Source account does not exist");
        }

        Account destinationAccount = AccountRepository.findById(request.getDestinationAccountId());
        if (destinationAccount == null) {
            throw new IllegalStateException("Destination account does not exist");
        }

        if (!destinationAccount.getCurrency().equals(sourceAccount.getCurrency())){
            throw new IllegalStateException("Cross currencies transfers doesn't supported");
        }
        validateAmount(request.getAmount());

    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalStateException("The amount must be set");
        }

        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new IllegalStateException("The amount must be positive; amount = " + amount);
        }

        if (TRANSACTION_MAX_AMOUNT.compareTo(amount) < 0) {
            throw new IllegalStateException(String.format(
                    "The amount must be less or equal max amount %s; requested amount: %s",
                    TRANSACTION_MAX_AMOUNT, amount));
        }

        if (amount.scale() > 2) {
            throw new IllegalStateException("The amount scale must be less or equal 2");
        }
    }
}
