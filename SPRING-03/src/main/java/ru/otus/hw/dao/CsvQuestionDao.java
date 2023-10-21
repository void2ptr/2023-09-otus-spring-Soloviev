package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.props.application.TestFileName;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final int SKIP_LINES = 1;

    private static final char FIELD_SEPARATOR = ';';

    private final TestFileName fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(fileNameProvider.getTestFileName());

        try (InputStreamReader streamReader = new InputStreamReader(resource.getInputStream(),
                StandardCharsets.UTF_8)) {
            CsvToBean<QuestionDto> csvToBeans = new CsvToBeanBuilder<QuestionDto>(streamReader)
                    .withSkipLines(SKIP_LINES)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(FIELD_SEPARATOR)
                    .withType(QuestionDto.class)
                    .build();
            for (QuestionDto dto : csvToBeans) {
                questions.add(dto.toDomainObject());
            }

        } catch (Exception e) {
            throw new QuestionReadException("Problem to read Question: ", e);
        }

        return questions;
    }
}
