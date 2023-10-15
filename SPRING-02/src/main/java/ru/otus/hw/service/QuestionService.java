package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionService {

    String getAskAnswerPrompt();

    String showQuestion(Question question);

    Boolean askQuestion(Question question);
}
