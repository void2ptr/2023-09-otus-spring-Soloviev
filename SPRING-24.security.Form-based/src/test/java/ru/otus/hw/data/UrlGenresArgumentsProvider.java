package ru.otus.hw.data;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UrlGenresArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return InitTestData.mixUrlsAndUsers(List.of(
                        "/genres",
                        "/genres/add",
                        "/genres/4/edit",
                        "/genres/4/delete"
                ))
                .stream()
                .map(Arguments::of);
    }
}
