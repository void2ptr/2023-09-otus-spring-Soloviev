package ru.otus.hw.service;

import ru.otus.hw.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findAuthorById(long id);

    AuthorDto insert(AuthorDto author);

    AuthorDto update(AuthorDto author);

    AuthorDto delete(long id);

}
