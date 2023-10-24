package ru.otus.hw.service.questions;

import ru.otus.hw.domain.Question;

public interface QuestionsService {

    String showQuestion(Question question);

    boolean isAnswerValid(Question question);
}
