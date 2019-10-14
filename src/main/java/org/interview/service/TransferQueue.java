package org.interview.service;

import org.interview.data.TransferTransaction;

import java.util.concurrent.ArrayBlockingQueue;

class TransferQueue {

    static final ArrayBlockingQueue<TransferTransaction> QUEUE = new ArrayBlockingQueue<>(10000);

}
