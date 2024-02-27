package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindByPosition;
import ru.otus.hw.model.Caterpillar;

@SuppressWarnings("unused")
public class CaterpillarDto {

    @CsvBindByPosition(position = 0)
    private String body;

    public Caterpillar toCaterpillar() {
        return new Caterpillar(body);
    }
}
