package ru.otus.hw.service.questions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.io.StreamsIOService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Service QuestionsServiceImpTest")
class QuestionsServiceImpTest {

    private final QuestionsConst questionsConst = new QuestionsConstImp();
    private final StreamsIOService mockStreamsIOService = mock(StreamsIOService.class);
    private final QuestionsServiceImp questionService = new QuestionsServiceImp(mockStreamsIOService,
            new QuestionsConstImp()
    );

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
        when(mockStreamsIOService.readStringWithPrompt(questionsConst.getAnswerPrompt())).thenReturn("2");

        // tested method
        Boolean actualAnswer = questionService.isAnswerValid(question);

        // test
        assertEquals(true, actualAnswer);
    }
}
