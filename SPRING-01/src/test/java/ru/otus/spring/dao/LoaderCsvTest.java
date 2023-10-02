package ru.otus.spring.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionImpl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс LoaderCsv")
class LoaderCsvTest {
    private static final String PATH = "classpath:data/questions.EN.csv";
    private static final Integer SKIP_LINES = 1;
    private static final List<Question> questions = new ArrayList<>();

    @BeforeAll
    static void setUp(){
        // Read FILE to ...
        try {
            File file = ResourceUtils.getFile(PATH);

            CSVReader csvReader = new CSVReaderBuilder( new FileReader(file) )
                    .withCSVParser( new CSVParserBuilder().withSeparator(';').build() )
                    .withSkipLines(SKIP_LINES)
                    .build();

            for (String[] row : csvReader.readAll()) {
                questions.add(new QuestionImpl(row));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getQuestions() {
        LoaderCsvImp loader = new LoaderCsvImp(PATH);

        for (Question expected : loader.getQuestions() ){
            Question actual = questions.get( Integer.valueOf( expected.getOrder()) - SKIP_LINES );
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
