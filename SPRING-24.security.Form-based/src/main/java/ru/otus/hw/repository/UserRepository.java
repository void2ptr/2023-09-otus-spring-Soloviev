package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
