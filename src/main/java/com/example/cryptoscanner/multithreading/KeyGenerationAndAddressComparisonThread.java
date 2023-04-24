package com.example.cryptoscanner.multithreading;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.params.MainNetParams;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@AllArgsConstructor
@Slf4j
public class KeyGenerationAndAddressComparisonThread implements Runnable {

    private static final MainNetParams MAIN_NET = new MainNetParams();

    private AtomicReference<BigInteger> counter;

    private Long beforeTime;

    private String low;

    private String high;

    private List<String> addresses;

    @Override
    public void run() {
        log.info("Start KeyGenerationAndAddressComparisonThread with name" + Thread.currentThread().getName());
        BigInteger bigIntegerLow = new BigInteger(low, 16);
        BigInteger bigIntegerHigh = new BigInteger(high, 16);


        for (BigInteger i = bigIntegerHigh; i.compareTo(bigIntegerLow) >= 0; i = i.subtract(BigInteger.ONE)) {
            counter.set(counter.get().add(BigInteger.ONE));
            log.info("COUNTER = " + counter.get());
            log.info("TIME = " + (System.currentTimeMillis() - beforeTime) / 1000);
            byte[] keyBytes = hexStringToByteArray(String.format("%064x", i));
            ECKey ecKey = ECKey.fromPrivate(keyBytes);
            LegacyAddress address = LegacyAddress.fromKey(MAIN_NET, ecKey);
//            log.info("address = " + address);
//            log.info("privateKey = " + String.format("%064x", ecKey.getPrivKey()));

            if (addresses.contains(address.toString())) {
                log.info("address = " + address);
                log.info("privateKey = " + String.format("%064x", ecKey.getPrivKey()));
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