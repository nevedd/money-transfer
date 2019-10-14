package org.interview.controller;

import io.javalin.http.Handler;
import org.interview.data.TransferRequest;
import org.interview.service.TransferQueueProducer;


public class TransferController {

    public static Handler transfer = ctx -> {
        var request = ctx.bodyAsClass(TransferRequest.class);
        try {
            TransferQueueProducer.produce(request);
            // Accepted
            ctx.status(202);
        } catch (IllegalStateException e) {
            ctx.result(e.getMessage());
            //Bad request
            ctx.status(400);
        } catch (Exception e){
            ctx.result(e.getMessage());
            ctx.status(500);
        }
    };

}
