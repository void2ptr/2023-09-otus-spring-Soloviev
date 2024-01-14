package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.UserNotFoundException;
import ru.otus.hw.model.User;
import ru.otus.hw.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDetails> findAll() {
        return userRepository.findAll()
                .stream()
                .collect(Collectors.toList());
    }

}
