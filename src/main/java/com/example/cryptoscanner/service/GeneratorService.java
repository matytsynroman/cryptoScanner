package com.example.cryptoscanner.service;

import com.example.cryptoscanner.runnable.KeyGenerationAndAddressComparisonThread;
import com.example.cryptoscanner.runnable.ThreadWrapper;
import com.example.cryptoscanner.runnable.TotalExecutionTimeLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class GeneratorService {

    private final AtomicReference<BigInteger> counter = new AtomicReference<>(BigInteger.ZERO);

    private final FileService fileService;

    @Autowired
    public GeneratorService(FileService fileService) {
        this.fileService = fileService;
    }

    public void findBTCAddressInRange(String reqRangeFilePath, String reqAddressFilePath, String reqKeyPairsFilePath) {
        try {

            log.info("Started find BTC address in range...");

            Long beforeTime = System.currentTimeMillis();

            List<String> fileRangeList = fileService.getListLines(reqRangeFilePath);

            List<String> listAddresses = Collections.synchronizedList(fileService.getListLines(reqAddressFilePath));

            int availableProcessors = Runtime.getRuntime().availableProcessors() - 2;

            log.info("Available processors: " + availableProcessors);

            String firstRange = fileRangeList.get(0);
            String endRange = fileRangeList.get(1);

            BigInteger low = new BigInteger(firstRange, 16);
            BigInteger high = new BigInteger(endRange, 16);
            BigInteger range = high.subtract(low);

            BigInteger separateRange = range.divide(BigInteger.valueOf(availableProcessors));

            List<BigInteger> parts = new ArrayList<>(availableProcessors + 1);

            for (int i = 0; i <= availableProcessors; i++) {
                if (i == 0) parts.add(low);
                else if (i == availableProcessors) parts.add(high);
                else parts.add(parts.get(i - 1).add(separateRange));
            }

            for (int i = 0; i <= availableProcessors; i++) {
                log.info("Part for processing #" + (i) + ": " + String.format("%064x", parts.get(i)));
            }

            ThreadWrapper[] threads = new ThreadWrapper[availableProcessors];

//            for (int i = 0; i < availableProcessors; i++) {
//                threads[i] = new Thread(new KeyGenerationAndAddressComparisonThread(counter, beforeTime, String.format("%064x", parts.get(i)), String.format("%064x", parts.get(i + 1)), listAddresses));
//                threads[i].start();
//            }
            for (int i = 0; i < availableProcessors; i++) {
                threads[i] = new ThreadWrapper(new KeyGenerationAndAddressComparisonThread(fileService, counter, beforeTime, String.format("%064x", parts.get(i)), String.format("%064x", parts.get(i + 1)), listAddresses, reqKeyPairsFilePath));
                new Thread(threads[i]).start();
            }

//            for (int i = 0; i < availableProcessors; i++) {
//                threads[i].join();
//            }

            TotalExecutionTimeLogger logger = new TotalExecutionTimeLogger(threads, counter);
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(logger, 0, 5, TimeUnit.SECONDS);

            Long afterTime = System.currentTimeMillis();
            log.info("Result time seconds = " + (afterTime - beforeTime) / 1000);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }


}
