package com.example.cryptoscanner.service;

import com.example.cryptoscanner.CryptoScannerApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService {

    private static String keyPairFilePath;

    static {
        try {
            keyPairFilePath = new File(
                    CryptoScannerApplication.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            ).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public List<String> getListLines(String reqFilePath) {

        ArrayList<String> listLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(reqFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listLines.add(line);
                log.info("Read line " + line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return listLines;
    }

    public void writeKeyPairs(String address, String privateKey) {
        File file = new File(keyPairFilePath + "/keyPairResult.txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, true);
                writer.write("address : privateKey");
            }
            FileWriter writer = new FileWriter(file, true);
            writer.write(address + " : " + privateKey);
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
