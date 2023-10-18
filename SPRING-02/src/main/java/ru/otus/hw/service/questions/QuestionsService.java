package ru.otus.hw.service.questions;

import ru.otus.hw.domain.Question;

public interface QuestionsService {

    String showQuestion(Question question);

    Boolean chooseAnswer(Question question);
}
