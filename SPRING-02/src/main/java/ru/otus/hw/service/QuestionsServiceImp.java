package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class QuestionsServiceImp implements QuestionsService {

    public static final String ASK_ANSWER_PROMPT = "Please input the correct answer number";

    private final IOService ioService;

    // getter for test only.
    // because lombok @Getter generate: [getASK_ANSWER_PROMPT()]
    // Hmm-m-m. Is it chosen correctly? [getAskAnswerPrompt()]
    public String getAskAnswerPrompt() {
        return ASK_ANSWER_PROMPT;
    }

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
        String userAnswer = ioService.readStringWithPrompt(ASK_ANSWER_PROMPT);
        if (correctAnswer.contentEquals(userAnswer)) {
            return true;
        } else {
            return false;
        }
    }
}
