package ru.otus.hw.dao;

import ru.otus.hw.dao.dto.CsvBean;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.AppProps;
import ru.otus.hw.dao.dto.ButterflyDto;
import ru.otus.hw.model.Butterfly;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
@Repository
@RequiredArgsConstructor
public class ButterflyRepository {

    private static final char FIELD_QUOTE = '\"';

    private static final char FIELD_SEPARATOR = ';';

    @Getter
    private final Collection<Butterfly> butterflies = new ArrayList<>();

    private final AppProps appProps;

    @SuppressWarnings("unused")
    public void saveButterflies(Collection<Butterfly> butterflies) {

        List<CsvBean> csvData = butterflies.stream()
                .map(ButterflyDto::new)
                .collect(Collectors.toList());

        try (Writer writer  = new FileWriter(appProps.getPathOutput())) {
            StatefulBeanToCsv<CsvBean> sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer)
                    .withQuotechar(FIELD_QUOTE)
                    .withSeparator(FIELD_SEPARATOR)
                    .build();

            sbc.write(csvData);
        } catch (Exception e) {
            throw new RuntimeException("Problem to write file: %s".formatted(e.getMessage()));
        }
    }
}
