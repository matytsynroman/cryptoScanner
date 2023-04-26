package com.example.cryptoscanner.runnable;

import com.example.cryptoscanner.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.params.MainNetParams;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
public class KeyGenerationAndAddressComparisonThread implements Runnable {

    @Autowired
    public KeyGenerationAndAddressComparisonThread(FileService fileService, AtomicReference<BigInteger> counter, Long beforeTime, String low, String high, List<String> addresses, String reqKeyPairsFilePath) {
        this.fileService = fileService;
        this.counter = counter;
        this.beforeTime = beforeTime;
        this.low = low;
        this.high = high;
        this.addresses = addresses;
        this.reqKeyPairsFilePath = reqKeyPairsFilePath;
    }


    private static final MainNetParams MAIN_NET = new MainNetParams();

    private AtomicReference<BigInteger> counter;

    private Long beforeTime;

    private String low;

    private String high;

    private List<String> addresses;

    private FileService fileService;

    private String reqKeyPairsFilePath;

    @Override
    public void run() {
        log.info("Start KeyGenerationAndAddressComparisonThread with name" + Thread.currentThread().getName());
        BigInteger bigIntegerLow = new BigInteger(low, 16);
        BigInteger bigIntegerHigh = new BigInteger(high, 16);

        for (BigInteger i = bigIntegerHigh; i.compareTo(bigIntegerLow) >= 0; i = i.subtract(BigInteger.ONE)) {

            if (addresses.isEmpty()) {
                break;
            }

            counter.set(counter.get().add(BigInteger.ONE));
//            log.info("COUNTER = " + counter.get());
//            log.info("TIME = " + (System.currentTimeMillis() - beforeTime) / 1000);
            byte[] keyBytes = hexStringToByteArray(String.format("%064x", i));
            ECKey ecKey = ECKey.fromPrivate(keyBytes);
            LegacyAddress address = LegacyAddress.fromKey(MAIN_NET, ecKey);

            if (addresses.contains(address.toString())) {
                log.info("address = " + address);
                log.info("privateKey = " + String.format("%064x", ecKey.getPrivKey()));
                fileService.writeKeyPairs(address.toString(), String.format("%064x", ecKey.getPrivKey()), reqKeyPairsFilePath);
                addresses.remove(address.toString());
            }
        }

    }

    private static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
