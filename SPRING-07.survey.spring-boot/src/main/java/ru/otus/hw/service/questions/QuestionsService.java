package ru.otus.hw.service.questions;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionsService {

    String showQuestion(Question question);

    boolean isAnswerValid(Question question);

    void randomizeQuestions(List<Question> questions);
}
