package com.example.cryptoscanner.runnable;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class TotalExecutionTimeLogger extends Thread {

    private final ThreadWrapper[] threads;

    private final AtomicReference<BigInteger> counter;

    private final ScheduledExecutorService executorService;

    private final CountDownLatch latch;

    public TotalExecutionTimeLogger(ThreadWrapper[] threads, AtomicReference<BigInteger> counter, ScheduledExecutorService executorService, CountDownLatch latch) {
        this.threads = threads;
        this.counter = counter;
        this.executorService = executorService;
        this.latch = latch;
    }

    @Override
    public void run() {
        long totalExecutionTime = 0;
        for (ThreadWrapper thread : threads) {
            totalExecutionTime += thread.getExecutionTime();
        }
        log.info("Total execution time: {} s", totalExecutionTime / threads.length);
        log.info("Total number of attempts to generate keys: {}", counter.get());

        if (latch.getCount() == 0) {
            executorService.shutdown();
        }
    }
}
