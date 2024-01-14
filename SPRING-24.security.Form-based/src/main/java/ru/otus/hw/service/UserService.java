package ru.otus.hw.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    List<UserDetails> findAll();

}
