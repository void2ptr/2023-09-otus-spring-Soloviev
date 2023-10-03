package ru.otus.spring.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс LoaderCsv")
class LoaderCsvTest {
    private static final String PATH = "data/questions.EN.csv";
    private static final List<Question> questions = new ArrayList<>();

    @BeforeAll
    static void setUp(){
        ClassPathResource resource = new ClassPathResource(PATH);

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                questions.add( new QuestionImpl(line.split(";")) );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getQuestions() {
        LoaderCsvImp loader = new LoaderCsvImp(PATH);

        for (Question expected : loader.getQuestions() ){
            Question actual = questions.get( Integer.valueOf( expected.getOrder()) - 1 );
            assertEquals( expected.getOrder()      , actual.getOrder() );
            assertEquals( expected.getDescription(), actual.getDescription() );
            assertEquals( expected.getQuestion1()  , actual.getQuestion1() );
            assertEquals( expected.getQuestion2()  , actual.getQuestion2() );
            assertEquals( expected.getQuestion3()  , actual.getQuestion3() );
            assertEquals( expected.getQuestion4()  , actual.getQuestion4() );
            assertEquals( expected.getAnswer()     , actual.getAnswer() );
        }
    }
}
