package ru.otus.hw.data;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UrlBooksArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return InitTestData.mixUrlsAndUsers(List.of(
                        "/books",
                        "/books/add",
                        "/books/4/edit",
                        "/books/4/delete"))
                .stream()
                .map(Arguments::of);
    }
}
