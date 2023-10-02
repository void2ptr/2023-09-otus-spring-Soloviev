package ru.otus.spring.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.util.ResourceUtils;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionImpl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LoaderCsvImp implements LoaderCsv {

    private final List<Question> questions = new ArrayList<>();

    public LoaderCsvImp(String path) {
        setQuestions(path);
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    private void setQuestions(String path) {
        try {
            File file = ResourceUtils.getFile(path);

            // Create an object of file reader class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvParser object with
            // custom separator semicolon
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            // create csvReader object with parameter
            // file reader and parser
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .withSkipLines(1) // Skip first line
                    .build();

            // Mapper Line to Object
            for (String[] row : csvReader.readAll()) {
                questions.add( new QuestionImpl(row) );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
