package org.interview.service;

import org.interview.data.Account;
import org.interview.repository.AccountRepository;

public class AccountCreator {

    public static void create(Account account){
        AccountValidator.validate(account);
        AccountRepository.create(account);
    }
}
