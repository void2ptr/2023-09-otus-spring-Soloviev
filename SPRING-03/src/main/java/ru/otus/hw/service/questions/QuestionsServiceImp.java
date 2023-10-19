package ru.otus.hw.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.props.translate.QuestionsProps;

@Service
@RequiredArgsConstructor
public class QuestionsServiceImp implements QuestionsService {

    private final IOService ioService;

    private final QuestionsProps questionsConst;

    @Override
    public String showQuestion(Question question) {
        int correctAnswer = -1;
        int actualAnswer = 0;

        ioService.printFormattedLine("%s ", question.text());
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("   %s. %s ", ++actualAnswer, answer.text());
            if (answer.isCorrect())  {
                correctAnswer = actualAnswer;
            }
        }

        return Integer.toString(correctAnswer);
    }

    @Override
    public Boolean chooseAnswer(Question question) {
        String correctAnswer = showQuestion(question);
        String userAnswer = ioService.readStringWithPrompt(questionsConst.getAnswerPrompt());
        return correctAnswer.contentEquals(userAnswer);
    }
}
