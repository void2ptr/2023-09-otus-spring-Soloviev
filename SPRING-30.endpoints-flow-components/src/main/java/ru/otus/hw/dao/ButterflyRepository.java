package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.props.InsectsRepositoryProps;
import ru.otus.hw.dao.dto.ButterflyDto;
import ru.otus.hw.dao.dto.CsvBean;
import ru.otus.hw.model.Butterfly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
@Repository
@RequiredArgsConstructor
public class ButterflyRepository {
    private static final int SKIP_LINES = 0;

    private static final char FIELD_QUOTE = '\"';

    private static final char FIELD_SEPARATOR = ';';

    private final InsectsRepositoryProps insectsRepositoryProps;

    @SuppressWarnings("unused")
    public void saveButterflies(List<Butterfly> butterflies) {
        List<CsvBean> csvData = butterflies.stream()
                .map(ButterflyDto::new)
                .collect(Collectors.toList());

        try (Writer writer  = new FileWriter(insectsRepositoryProps.getPathOutput())) {
            StatefulBeanToCsv<CsvBean> sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer)
                    .withQuotechar(FIELD_QUOTE)
                    .withSeparator(FIELD_SEPARATOR)
                    .build();

            sbc.write(csvData);
        } catch (Exception e) {
            throw new RuntimeException("Problem to write file: %s".formatted(e.getMessage()));
        }
    }

    public List<Butterfly> findButterflies() {
        List<Butterfly> butterflies = new ArrayList<>();
        File fileUrl = new File(insectsRepositoryProps.getPathOutput());

        try (InputStreamReader streamReader = new InputStreamReader(new FileInputStream(fileUrl),
                StandardCharsets.UTF_8)) {
            CsvToBean<ButterflyDto> csvToBeans = new CsvToBeanBuilder<ButterflyDto>(streamReader)
                    .withSkipLines(SKIP_LINES)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(FIELD_SEPARATOR)
                    .withType(ButterflyDto.class)
                    .build();
            for (ButterflyDto dto : csvToBeans) {
                butterflies.add(dto.toButterfly());
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem to read file: %s".formatted(e.getMessage()));
        }

        return butterflies;
    }
}
