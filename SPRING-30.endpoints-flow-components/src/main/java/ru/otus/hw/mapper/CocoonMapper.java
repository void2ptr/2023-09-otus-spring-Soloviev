package ru.otus.hw.mapper;

import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Cocoon;
import ru.otus.hw.model.Pupa;


@SuppressWarnings("unused")
public class CocoonMapper {

    public static Pupa toPupa(Cocoon cocoon) {
        return new Pupa(cocoon.getSilk().toLowerCase());
    }


    public static Butterfly toButterfly(Pupa pupa) {
        return new Butterfly(pupa.getNimfa().toUpperCase());
    }


}
