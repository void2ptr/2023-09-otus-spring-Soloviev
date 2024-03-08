package ru.otus.hw.mapper;

import io.r2dbc.spi.Row;

import java.util.function.BiFunction;

public class BookR2ExistMapper implements BiFunction<Row, Object, Boolean> {

    @Override
    public Boolean apply(Row row, Object o) {
        return row.get("exists", Boolean.class);
    }
}
