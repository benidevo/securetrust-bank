package com.stb.bankaccountservice.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    private static final String ACCOUNT_NUMBER_PREFIX = "2";

    public String generateAccountNumber(String accountName) {
        String combinedInput = accountName.trim() + System.currentTimeMillis();
        byte[] hashBytes = generateHash(combinedInput);
        String truncatedHash = truncateHash(hashBytes);
        String paddedAccountNumber = padAccountNumber(truncatedHash);
        return ACCOUNT_NUMBER_PREFIX + paddedAccountNumber;
    }

    private String padAccountNumber(String accountNumber) {
        if (accountNumber.length() >= ACCOUNT_NUMBER_LENGTH) {
            return accountNumber.substring(0, ACCOUNT_NUMBER_LENGTH);
        } else {
            int paddingLength = ACCOUNT_NUMBER_LENGTH - accountNumber.length();
            StringBuilder paddedNumber = new StringBuilder(accountNumber);
            for (int i = 0; i < paddingLength; i++) {
                int randomDigit = (int) (Math.random() * 10);
                paddedNumber.append(randomDigit);
            }
            return paddedNumber.toString();
        }
    }

    private byte[] generateHash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

    private String truncateHash(byte[] hashBytes) {
        String hashString = bytesToHexString(hashBytes);
        int endIndex = Math.min(hashString.length(), ACCOUNT_NUMBER_LENGTH);
        return hashString.substring(0, endIndex).replaceAll("[^0-9]", "");
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
