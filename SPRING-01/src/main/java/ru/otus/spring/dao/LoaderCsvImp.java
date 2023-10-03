package ru.otus.spring.dao;

import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.QuestionImpl;

import java.io.*;
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
        ClassPathResource resource = new ClassPathResource(path);

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
}
