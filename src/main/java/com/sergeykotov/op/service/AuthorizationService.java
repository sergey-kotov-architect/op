package com.sergeykotov.op.service;

import com.sergeykotov.op.exception.AuthorizationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class AuthorizationService {
    private static final String API_KEY = "2718258bae2d00062aaf47b91bafa4351914f2c63b24d72f64a1f92e0bf5548f";

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void authorize(String authorization) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
        byte[] hashBytes = digest.digest(authorization.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(hashBytes);
        if (!API_KEY.equals(hash)) {
            throw new AuthorizationException();
        }
    }
}