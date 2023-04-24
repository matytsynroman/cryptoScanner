package com.example.cryptoscanner.controller;

import com.example.cryptoscanner.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CryptoController {

    @Autowired
    private GeneratorService generatorService;

    @GetMapping("/find-btc-address-in-range")
    public void generateAndValidSign(@RequestParam(name = "reqRangeFilePath") String reqRangeFilePath, @RequestParam(name = "reqAddressFilePath") String reqAddressFilePath) {
        generatorService.findBTCAddressInRange(reqRangeFilePath, reqAddressFilePath);
    }
}
