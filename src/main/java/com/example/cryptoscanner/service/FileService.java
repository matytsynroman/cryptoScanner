package com.example.cryptoscanner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService {

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

    public void writeKeyPairs(String address, String privateKey, String reqKeyPairsFilePath) {
        File file = new File(reqKeyPairsFilePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(address + " : " + privateKey);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("Error ", e);
        }
    }
}
