package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.NoArgsConstructor;
import ru.otus.hw.model.Butterfly;

@SuppressWarnings("unused")
@NoArgsConstructor
public class ButterflyDto extends CsvBean {

    @CsvBindByPosition(position = 0)
    private String kind;

    public ButterflyDto(Butterfly butterfly) {
        this.kind = butterfly.getKind();
    }

    public Butterfly toButterfly() {
        return new Butterfly(kind);
    }
}
