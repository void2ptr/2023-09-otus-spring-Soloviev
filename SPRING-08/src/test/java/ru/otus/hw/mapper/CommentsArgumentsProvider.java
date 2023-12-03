package ru.otus.hw.mapper;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.otus.hw.dao.InitTestData;

import java.util.stream.Stream;

public class CommentsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return InitTestData.getDbComments()
                .stream()
                .map(Arguments::of);
    }
}
