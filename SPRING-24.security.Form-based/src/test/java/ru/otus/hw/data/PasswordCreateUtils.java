package ru.otus.hw.data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Encode password for DB users
 */
public class PasswordCreateUtils {
    public static void main(String a[]) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bcryptPasswordEncoder.encode("password");
        System.out.println(pwd);
    }
}
