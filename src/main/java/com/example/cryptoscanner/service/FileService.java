package com.example.cryptoscanner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileService {

    public List<String> getListLines(String reqFilePath) {

        ArrayList<String> listLines = new ArrayList<>();

        String filePath = reqFilePath;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
}
