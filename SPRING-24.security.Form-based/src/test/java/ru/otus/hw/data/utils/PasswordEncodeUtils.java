package ru.otus.hw.data.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Encode password for DB users
 */
public class PasswordEncodeUtils {
    public static void main(String[] args) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bcryptPasswordEncoder.encode("password");
        System.out.println(pwd);
    }
}
