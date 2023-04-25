package com.example.cryptoscanner.runnable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadWrapper implements Runnable {

    private final Runnable target;
    private long startTime = System.currentTimeMillis();

    public ThreadWrapper(Runnable target) {
        this.target = target;
    }

    @Override
    public void run() {
        target.run();
        log.info("Execution time: {} s", (System.currentTimeMillis() - startTime) / 1000);
    }

    public long getExecutionTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
