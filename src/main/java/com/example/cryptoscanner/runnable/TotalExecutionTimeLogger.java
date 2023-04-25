package com.example.cryptoscanner.runnable;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class TotalExecutionTimeLogger implements Runnable {

    private final ThreadWrapper[] threads;

    private AtomicReference<BigInteger> counter;

    public TotalExecutionTimeLogger(ThreadWrapper[] threads, AtomicReference<BigInteger> counter) {
        this.threads = threads;
        this.counter = counter;
    }

    @Override
    public void run() {
        long totalExecutionTime = 0;
        for (ThreadWrapper thread : threads) {
            totalExecutionTime += thread.getExecutionTime();
        }
        log.info("Total execution time: {} s", totalExecutionTime / threads.length);
        log.info("Total number of attempts to generate keys: {}", counter.get());
    }
}
