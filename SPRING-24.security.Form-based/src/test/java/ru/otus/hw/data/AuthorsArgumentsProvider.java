//package ru.otus.hw.data;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//
//import java.util.stream.Stream;
//
//
//@RequiredArgsConstructor
//public class AuthorsArgumentsProvider implements ArgumentsProvider {
//
//    private final InitTestData initTestData;
//
//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
//        return initTestData.getDbAuthors()
//                .stream()
//                .map(Arguments::of);
//    }
//}
