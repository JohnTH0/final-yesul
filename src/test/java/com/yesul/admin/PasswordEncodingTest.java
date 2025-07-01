package com.yesul.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordEncodeTest {

    @Test
    void printEncodedPassword() {
        String rawPassword = "password123";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Encoded password: " + encodedPassword);
    }
}