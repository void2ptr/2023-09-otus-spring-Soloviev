package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

public interface BookRepositoryCustom {

    void updateCommentsByBook(Book book);

    void removeCommentsByBookId(String id);

}
