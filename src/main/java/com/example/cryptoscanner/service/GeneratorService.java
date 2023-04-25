package com.example.cryptoscanner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public void findBTCAddressInRange(String reqRangeFilePath, String reqAddressFilePath) {
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

            Thread[] threads = new Thread[availableProcessors];

//            for (int i = 0; i < availableProcessors; i++) {
//                threads[i] = new Thread(new KeyGenerationAndAddressComparisonThread(counter, beforeTime, String.format("%064x", parts.get(i)), String.format("%064x", parts.get(i + 1)), listAddresses));
//                threads[i].start();
//            }
//
//            for (int i = 0; i < availableProcessors; i++) {
//                threads[i].join();
//            }

            Long afterTime = System.currentTimeMillis();
            log.info("Result time seconds = " + (afterTime - beforeTime) / 1000);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }


}
