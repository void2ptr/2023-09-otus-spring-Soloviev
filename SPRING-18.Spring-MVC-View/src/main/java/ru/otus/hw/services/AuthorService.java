package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findAuthorById(long id);

    void insert(AuthorDto authorDto);

    void update(AuthorDto authorDto);

    void delete(long authorId);

}
