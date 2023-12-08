package ru.otus.hw.tests_data_source.argument_provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.otus.hw.tests_data_source.InitTestData;

import java.util.stream.Stream;

public class CommentsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return InitTestData.getDbComments()
                .stream()
                .map(Arguments::of);
    }
}
