package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findAuthorById(long id);

    Optional<AuthorDto> insert(AuthorDto authorDto);

    Optional<AuthorDto> update(AuthorDto authorDto);

    boolean delete(long authorId);

}
