package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface LoaderCsv {
    List<Question> getQuestions();
}
