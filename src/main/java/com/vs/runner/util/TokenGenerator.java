package com.vs.runner.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    private static final int TOKEN_LENGTH = 32;

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
