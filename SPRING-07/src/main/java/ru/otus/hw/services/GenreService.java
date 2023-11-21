package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreService {

    @Transactional(readOnly = true)
    List<Genre> findAll();
}
