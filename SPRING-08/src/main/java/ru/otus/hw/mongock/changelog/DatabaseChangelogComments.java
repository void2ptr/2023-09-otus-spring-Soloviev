package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class DatabaseChangelogComments {

    List<String> opinions = Arrays.asList(
            "Хорошая книга",
            "Очень хорошая книга",
            "Классика мировой литературы",
            "Перечитывал много раз",
            "Выдержала проверку временем",
            "Это произведение, поможет Вам пережить любые трудности!",
            "Классика мировой литературы",
            "Книга - огонь!",
            "Автор - безусловно гений!",
            "Моя настольная книга",
            "Обязательна к прочтению",
            "Великолепно, автор гений!",
            "Книга написана на основании личного опыта",
            "Бестселлер",
            "Шедевр"
    );

    @ChangeSet(order = "000", id = "insertComments", author = "solo", runAlways = true)
    public void commentsGenerator(BookRepository bookRepository, CommentRepository commentRepository) {
        List<Book> books = bookRepository.findAll();
        SecureRandom r = new SecureRandom();
        books.forEach(book -> {
            int size = r.nextInt(opinions.size());
            List<String> commentsList = IntStream.range(0, size)
                    .boxed()
                    .map(id -> opinions.get(r.nextInt(opinions.size())))
                    .collect(Collectors.toList());
            commentRepository.save(new Comment(book, commentsList));
        });
    }

}
