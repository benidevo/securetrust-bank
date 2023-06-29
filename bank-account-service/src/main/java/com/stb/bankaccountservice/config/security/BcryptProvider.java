package com.stb.bankaccountservice.config.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptProvider {
    private final BCryptPasswordEncoder encoder;

    public BcryptProvider() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String hash(String plainText) {
        return encoder.encode(plainText);
    }

    public boolean isMatch(String plainText, String hashedText) {
        return encoder.matches(plainText, hashedText);
    }
}
