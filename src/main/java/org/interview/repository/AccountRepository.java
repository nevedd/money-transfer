package org.interview.repository;

import org.interview.data.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {

    private static final ConcurrentHashMap<Long, Account> storage = new ConcurrentHashMap<>();

    public static void create(Account account) {
        var id = account.getId();
        if (storage.containsKey(id)) {
            throw new IllegalStateException("Account with the same id already exists");
        }
        storage.put(id, account);
    }

    public static List<Account> findAll() {
        return new ArrayList<>(storage.values());
    }

    public static Account findById(Long id) {
        return storage.get(id);
    }

}
