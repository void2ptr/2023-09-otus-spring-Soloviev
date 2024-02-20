package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Butterfly;
import ru.otus.hw.model.Caterpillar;
import ru.otus.hw.model.Cocoon;
import ru.otus.hw.model.Pupa;


@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class MetamorphosesService {

    public Cocoon startCocoon(Caterpillar caterpillar) {
        return new Cocoon(caterpillar.getBody());
    }

    public Pupa startPupa(Cocoon cocoon) {
        return new Pupa(cocoon.getSilk());
    }

    public Butterfly startButterfly(Pupa pupa) {
        return new Butterfly(pupa.getNimfa());
    }
}
