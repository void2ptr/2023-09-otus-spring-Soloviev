package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.props.translate.QuestionsProps;
import ru.otus.hw.config.props.translate.QuestionsPropsImp;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.StreamsIOService;

import ru.otus.hw.service.questions.QuestionsServiceImp;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private final QuestionsPropsImp questionsConst = mock(QuestionsPropsImp.class);
    private final StreamsIOService mockStreamsIOService = mock(StreamsIOService.class);
    private final CsvQuestionDao mockCsvQuestionDao = mock(CsvQuestionDao.class);
    private final QuestionsServiceImp mockQuestionService = mock(QuestionsServiceImp.class);
    private final QuestionsProps mockQuestionsProps = mock(QuestionsPropsImp.class);
    private final TestServiceImpl testServiceImpl = new TestServiceImpl(
            mockStreamsIOService,
            mockCsvQuestionDao,
            mockQuestionService,
            mockQuestionsProps
    );

    @Test
    void executeTestFor() {
        // init
        Student student = new Student("Wu", "Foo");
        TestResult expectedResult = new TestResult(student);

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer A ?", false));
        answers.add(new Answer("Answer B ?", false));
        answers.add(new Answer("Answer C ?", true));
        Question question = new Question("The question", answers);

        List<Question> questions = new ArrayList<>();
        questions.add(question);
        expectedResult.applyAnswer(question, true);

        // mock
        when(questionsConst.getAnswerPrompt()).thenReturn("Start thr test:");
        when(mockStreamsIOService.readStringWithPrompt(questionsConst.getAnswerPrompt())).thenReturn("3");
        when(mockCsvQuestionDao.findAll()).thenReturn(questions);
        when(mockQuestionService.chooseAnswer(question)).thenReturn(true);

        // tested method
        TestResult actualResult = testServiceImpl.executeTestFor(student);

        // test
        assertEquals(expectedResult.getAnsweredQuestions(),actualResult.getAnsweredQuestions());
        assertEquals(expectedResult.getRightAnswersCount(),actualResult.getRightAnswersCount());
    }
}
