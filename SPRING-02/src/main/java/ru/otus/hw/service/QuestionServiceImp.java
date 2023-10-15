package ru.otus.hw.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class QuestionServiceImp implements QuestionService {

    private final IOService ioService;

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
    public Boolean askQuestion(String prompt, Question question) {
        return ioService.readStringWithPrompt(prompt)
                .contentEquals(
                        showQuestion(question)
                );
    }
}
