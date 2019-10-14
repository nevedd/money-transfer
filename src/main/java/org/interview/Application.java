package org.interview;

import org.interview.controller.AccountController;
import org.interview.controller.TransactionController;
import org.interview.controller.TransferController;
import io.javalin.Javalin;
import org.interview.service.TransferQueueConsumer;


public class Application {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        createControllers(app);
        createTransferProcessor();
    }

    private static void createTransferProcessor() {
        TransferQueueConsumer transferQueueConsumer = new TransferQueueConsumer();
        transferQueueConsumer.run();
    }

    private static void createControllers(Javalin app) {
        app.get("/account/all", AccountController.getAll);
        app.get("/account/:id", AccountController.getById);
        app.post("/account", AccountController.create);
        app.post("/transfer", TransferController.transfer);
        app.get("/transaction/all", TransactionController.getAll);
    }
}
