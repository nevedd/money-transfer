package org.interview.repository;

import org.interview.data.TransferRequest;
import org.interview.data.TransferStatus;
import org.interview.data.TransferTransaction;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TransferTransactionRepository {

    private static final ConcurrentHashMap<Long, TransferTransaction> storage = new ConcurrentHashMap<>();

    public static TransferTransaction createByTransferRequest(TransferRequest request) {
        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setId(System.nanoTime());
        transferTransaction.setStatus(TransferStatus.NEW);
        transferTransaction.setTransferRequest(request);
        transferTransaction.setUpdateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        transferTransaction.setCreateDateTime(ZonedDateTime.now(ZoneOffset.UTC));
        storage.put(transferTransaction.getId(), transferTransaction);
        return transferTransaction;
    }

    public static TransferTransaction findById(Long id) {
        return storage.get(id);
    }

    public static List<TransferTransaction> getAll() {
        return new ArrayList<>(storage.values());
    }

}
