package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.PropsApplicationData;
import ru.otus.hw.dao.dto.CaterpillarDto;
import ru.otus.hw.model.Caterpillar;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Repository
@RequiredArgsConstructor
public class CaterpillarRepository {
    private static final int SKIP_LINES = 1;

    private static final char FIELD_SEPARATOR = ';';

    private final List<Caterpillar> caterpillars = new ArrayList<>();

    private final PropsApplicationData propsApplicationData;

    public List<Caterpillar> findCaterpillar() {
        ClassPathResource resource = new ClassPathResource(propsApplicationData.getPathInput());

        try (InputStreamReader streamReader = new InputStreamReader(resource.getInputStream(),
                StandardCharsets.UTF_8)) {
            CsvToBean<CaterpillarDto> csvToBeans = new CsvToBeanBuilder<CaterpillarDto>(streamReader)
                    .withSkipLines(SKIP_LINES)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(FIELD_SEPARATOR)
                    .withType(CaterpillarDto.class)
                    .build();
            for (CaterpillarDto dto : csvToBeans) {
                caterpillars.add(dto.toCaterpillar());
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem to read file: %s".formatted(e.getMessage()));
        }

        return caterpillars;
    }

}
