package com.example.cryptoscanner;

import com.example.cryptoscanner.service.GeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CryptoScannerApplication implements CommandLineRunner {

    private final GeneratorService generatorService;

    @Autowired
    public CryptoScannerApplication(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CryptoScannerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            generatorService.findBTCAddressInRange(args[0], args[1]);
        } catch (Exception e) {
            log.error("CryptoScannerApplication error", e);
        }

    }
}
