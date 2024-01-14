//package ru.otus.hw.data;
//
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//
//import java.util.stream.Stream;
//
//public class GenresArgumentsProvider  implements ArgumentsProvider {
//
//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
//        return InitTestData.getDbGenres()
//                .stream()
//                .map(Arguments::of);
//    }
//}
