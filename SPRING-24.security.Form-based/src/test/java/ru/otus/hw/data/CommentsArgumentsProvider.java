//package ru.otus.hw.data;
//
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//import org.junit.jupiter.params.provider.Arguments;
//
//import java.util.stream.Stream;
//
//public class CommentsArgumentsProvider implements ArgumentsProvider {
//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
//        return InitTestData.getDbComments()
//                .stream()
//                .map(Arguments::of);
//    }
//}
