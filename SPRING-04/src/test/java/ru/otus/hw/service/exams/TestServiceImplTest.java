package ru.otus.hw.service.exams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.StreamsIOService;
import ru.otus.hw.service.questions.QuestionsServiceImpl;
import ru.otus.hw.service.translate.MessagesTranslator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@SpringBootTest(classes = TestServiceImpl.class)
class TestServiceImplTest {

    @MockBean
    private StreamsIOService mockStreamsIOService;
    @MockBean
    private CsvQuestionDao mockCsvQuestionDao;
    @MockBean
    private QuestionsServiceImpl mockQuestionService;
    @MockBean
    private MessagesTranslator mockTranslator;
    @Autowired
    private TestServiceImpl testServiceImpl;

    @Test
    void executeTestFor() {
        // init
        Student student = new Student("Wu", "Foo");

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer A ?", false));
        answers.add(new Answer("Answer B ?", false));
        answers.add(new Answer("Answer C ?", true));
        Question question = new Question("The question", answers);

        List<Question> questions = new ArrayList<>();
        questions.add(question);

        TestResult expected = new TestResult(student);
        expected.applyAnswer(question, true);

        // Mock
//        given(mockAppConfig.getTestFileName()).willReturn("data/questions.csv");
        given(mockTranslator.getProps("prompt.answer")).willReturn("Answer the question:");
        given(mockStreamsIOService.readStringWithPrompt("prompt.answer")).willReturn("3");
        given(mockCsvQuestionDao.findAll()).willReturn(questions);
        given(mockQuestionService.isAnswerValid(question)).willReturn(true);

        // tested method
        TestResult actual = testServiceImpl.executeTestFor(student);

        // test
        assertEquals(expected.getAnsweredQuestions(), actual.getAnsweredQuestions());
        assertEquals(expected.getRightAnswersCount(), actual.getRightAnswersCount());
    }
}
