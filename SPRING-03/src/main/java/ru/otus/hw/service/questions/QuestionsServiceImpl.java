package ru.otus.hw.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.translate.PropsTranslator;

@Service
@RequiredArgsConstructor
public class QuestionsServiceImpl implements QuestionsService {

    private final IOService ioService;

    private final PropsTranslator translate;

    @Override
    public String showQuestion(Question question) {
        int correctAnswer = -1;
        int actualAnswer = 0;

        String colorStart  = translate.getProps("question.color.start");
        String colorFinish = translate.getProps("question.color.finish");

        ioService.printFormattedLine("%s%s%s",
                colorStart,
                question.text(),
                colorFinish
        );
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("%s   %s. %s%s",
                    colorStart,
                    ++actualAnswer,
                    answer.text(),
                    colorFinish
            );
            if (answer.isCorrect())  {
                correctAnswer = actualAnswer;
            }
        }

        return Integer.toString(correctAnswer);
    }

    @Override
    public Boolean isAnswerValid(Question question) {
        String correctAnswer = showQuestion(question);
        String userAnswer = ioService.readStringWithPrompt(translate.getProps("prompt.question"));
        return correctAnswer.contentEquals(userAnswer);
    }
}
