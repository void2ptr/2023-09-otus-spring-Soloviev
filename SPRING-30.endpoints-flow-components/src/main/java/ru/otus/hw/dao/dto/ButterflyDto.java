package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindByPosition;
import ru.otus.hw.model.Butterfly;

@SuppressWarnings("unused")
public class ButterflyDto extends CsvBean {

    @CsvBindByPosition(position = 0)
    private final String kind;

    public ButterflyDto(Butterfly butterfly) {
        this.kind = butterfly.getKind();
    }
}
