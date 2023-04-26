package com.example.cryptoscanner.runnable;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ThreadWrapper extends Thread {

    private final Runnable thread;
    private final long startTime = System.currentTimeMillis();
    private CountDownLatch latch;

    public ThreadWrapper(Runnable thread, CountDownLatch latch) {
        this.thread = thread;
        this.latch = latch;
    }

    @Override
    public void run() {
        thread.run();
        log.info("Execution time: {} s", (System.currentTimeMillis() - startTime) / 1000);
        latch.countDown();
    }

    public long getExecutionTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
