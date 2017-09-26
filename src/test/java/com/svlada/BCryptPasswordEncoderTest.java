package com.svlada;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String clientSecret = bCryptPasswordEncoder.encode("clientSecret");
        boolean matches = bCryptPasswordEncoder.matches("clientSecret", clientSecret);
        System.out.println("matches = " + matches);
        System.out.println("clientSecret = " + clientSecret);
    }
}
