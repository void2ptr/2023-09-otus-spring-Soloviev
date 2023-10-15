package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionService {

    String showQuestion(Question question);

    Boolean askQuestion(String prompt, Question question);
}
