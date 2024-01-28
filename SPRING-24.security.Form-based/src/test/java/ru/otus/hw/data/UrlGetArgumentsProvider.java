package ru.otus.hw.data;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UrlGetArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return InitTestData.mixUrlsAndUsers(List.of(
                        "/authors",
                        "/authors/add",
                        "/authors/4/edit",
                        "/authors/4/delete",
                        "/books",
                        "/books/add",
                        "/books/4/edit",
                        "/books/4/delete",
                        "/books/1/comments",
                        "/books/1/comments/add",
                        "/books/1/comments/4/edit",
                        "/books/1/comments/4/delete",
                        "/genres",
                        "/genres/add",
                        "/genres/4/edit",
                        "/genres/4/delete"
                        ))
                .stream()
                .map(Arguments::of);
    }
}
