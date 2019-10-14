package org.interview.controller;

import org.interview.data.Account;
import io.javalin.http.Handler;
import org.interview.repository.AccountRepository;
import org.interview.service.AccountCreator;

import java.util.Objects;

public class AccountController {

    public static Handler getById = ctx -> {
        var id = Long.parseLong(Objects.requireNonNull(ctx.pathParam("id")));
        Account account = AccountRepository.findById(id);
        if (account != null) {
            ctx.json(account);
        } else {
            // Not Found
            ctx.result("Requested account does not exist");
            ctx.status(404);
        }
    };

    public static Handler getAll = ctx -> ctx.json(AccountRepository.findAll());

    public static Handler create = ctx -> {
        try {
            AccountCreator.create(ctx.bodyAsClass(Account.class));
        } catch (IllegalStateException e) {
            //Bad request
            ctx.result(e.getMessage());
            ctx.status(400);
        } catch (Exception e) {
            ctx.result(e.getMessage());
            ctx.status(500);
        }
    };

}
