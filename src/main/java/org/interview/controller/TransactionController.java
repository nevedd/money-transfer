package org.interview.controller;

import io.javalin.http.Handler;
import org.interview.repository.TransferTransactionRepository;

public class TransactionController {

    public static Handler getAll = ctx -> ctx.json(TransferTransactionRepository.getAll());
}
