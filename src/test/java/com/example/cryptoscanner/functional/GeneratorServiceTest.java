package com.example.cryptoscanner.functional;

import com.example.cryptoscanner.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GeneratorServiceTest {

    @Autowired
    private GeneratorService generatorService;


    @Test
    void contextLoads() throws URISyntaxException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL urlRange = classLoader.getResource("range.txt");
        URL urlAddresses = classLoader.getResource("addresses.txt");
        assert urlRange != null;
        assert urlAddresses != null;

        generatorService.findBTCAddressInRange(urlRange.getPath(), urlAddresses.getPath());

        File file = Paths.get(classLoader.getResource("keyPairResult.txt").toURI()).toFile();

        if (file.exists()) {
            assertTrue(file.delete());
        }

        assertTrue(file.createNewFile());

    }
}
