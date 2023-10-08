package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        List<Question> questions = new ArrayList<>();

        try {
            int SKIP_LINES = 1;
            ClassPathResource resource = new ClassPathResource(fileNameProvider.getTestFileName());
            InputStreamReader streamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            CsvToBean<QuestionDto> csvToBeans = new CsvToBeanBuilder<QuestionDto>(streamReader)
                    .withSkipLines(SKIP_LINES)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build();

            for (QuestionDto dto : csvToBeans) {
                questions.add(dto.toDomainObject());
            }

        } catch (Exception e){
            throw new QuestionReadException("CsvQuestionDao(): ", e);
        }

        return questions;
    }
}
