package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Service QuestionServiceImpTest")
class QuestionServiceImpTest {

    private final static String ASK_ANSWER_PROMPT = "Please input the correct answer number:";
    private final static StreamsIOService stubStreamsIOService = mock(StreamsIOService.class);
    private final static QuestionServiceImp questionService = new QuestionServiceImp(stubStreamsIOService);

    @DisplayName("Show Question: [showQuestion()]")
    @Test
    void showQuestion() {
        // init
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer 1 ?", false));
        answers.add(new Answer("Answer 2 ?", true));
        answers.add(new Answer("Answer 3 ?", false));
        Question question = new Question("The question", answers);

        // tested method
        String correctAnswer = questionService.showQuestion(question);

        // test
        assertNotEquals("1", correctAnswer);
        assertEquals("2", correctAnswer);
        assertNotEquals("3", correctAnswer);
    }

    @DisplayName("Show Question & ask Answer: [askQuestion()]")
    @Test
    void askQuestion() {
        // init
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer X ?", false));
        answers.add(new Answer("Answer Y ?", true));
        answers.add(new Answer("Answer Z ?", false));
        Question question = new Question("The question", answers);

        // mock
        when(stubStreamsIOService.readStringWithPrompt(ASK_ANSWER_PROMPT)).thenReturn("2");

        // tested method
        Boolean actualAnswer = questionService.askQuestion(ASK_ANSWER_PROMPT, question);

        // test
        assertEquals(true, actualAnswer);
    }
}
