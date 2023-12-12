package ru.otus.hw.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.helper.AnsiColors;
import ru.otus.hw.service.translate.MessagesTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionsServiceImpl implements QuestionsService {

    private final IOService ioService;

    private final MessagesTranslator translator;

    @Override
    public String showQuestion(Question question) {
        int correctAnswer = -1;
        int actualAnswer = 0;


        ioService.printFormattedLine("%s%s%s",
                AnsiColors.YELLOW,
                question.text(),
                AnsiColors.RESET
        );
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("%s   %s. %s%s",
                    AnsiColors.GREEN,
                    ++actualAnswer,
                    answer.text(),
                    AnsiColors.RESET
            );
            if (answer.isCorrect()) {
                correctAnswer = actualAnswer;
            }
        }

        return Integer.toString(correctAnswer);
    }

    @Override
    public boolean isAnswerValid(Question question) {
        String correctAnswer = showQuestion(question);
        String userAnswer = ioService.readStringWithPrompt(
                translator.getProps("prompt.answer", AnsiColors.YELLOW, AnsiColors.RESET)
        );
        return correctAnswer.contentEquals(userAnswer);
    }

    private <T> void randomizeList(List<T> listOrig, List<T> listRand) {

        if (listOrig.isEmpty()) {
            return;
        }
        Random rand = new Random();
        T element = listOrig.get(rand.nextInt(listOrig.size()));
        listRand.add(element);
        listOrig.remove(element);
        randomizeList(listOrig, listRand);
    }

    @Override
    public void randomizeQuestions(List<Question> questions) {
        List<Question> randQuestions = new ArrayList<>();
        randomizeList(questions, randQuestions);
        for (var question : randQuestions) {
            List<Answer> randAnswers = new ArrayList<>();
            randomizeList(question.answers(), randAnswers);
            questions.add(new Question(question.text(), randAnswers));
        }
    }

}
