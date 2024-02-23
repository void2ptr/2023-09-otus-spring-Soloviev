package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.BookService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/books")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "/books/books";
    }

    @GetMapping("/books/add")
    public String addPage(Model model) {
        long bookId = 0;
        BookDto bookDto = new BookDto(bookId, "New Book", null, null);
        List<AuthorDto> authorsDto = this.findAuthorsNotInBook(bookId);
        List<GenreDto> genresDto = this.findGenresNotInBook(bookId);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        return "/books/book-add";
    }

    @GetMapping("/books/{id}/edit")
    public String editPage(@PathVariable("id") long bookId, Model model) {
        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(bookId)));
        List<AuthorDto> authorsDto = this.findAuthorsNotInBook(bookId);
        List<GenreDto> genresDto = this.findGenresNotInBook(bookId);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        return "/books/book-edit";
    }

    @GetMapping("/books/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(id)));
        List<AuthorDto> authorsDto = this.findAuthorsNotInBook(id);
        List<GenreDto> genresDto = this.findGenresNotInBook(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("authors", authorsDto);
        return "/books/book-delete";
    }

    @PostMapping("/books/add")
    public String insert(BookIdsDto bookIdsDto) {
        bookService.insert(new Book(0L, bookIdsDto.getTitle(),
                this.findAuthor(bookIdsDto.getAuthorId()),
                this.findGenres(bookIdsDto.getGenresId())));
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/edit")
    public String update(BookIdsDto bookIdsDto) {
        bookService.update(new Book(bookIdsDto.getId(), bookIdsDto.getTitle(),
                this.findAuthor(bookIdsDto.getAuthorId()),
                this.findGenres(bookIdsDto.getGenresId())));
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    private Author findAuthor(long authorId) {
        var author = authorRepository.findAuthorById(authorId);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %d not found".formatted(authorId));
        }
        return author.get();
    }

    private List<Genre> findGenres(List<Long> genresIds) {
        var genres = genreRepository.findGenresByIdIn(genresIds);
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres with ids [%s] not found" .formatted(genresIds));
        }
        return genres;
    }

    private List<AuthorDto> findAuthorsNotInBook(long bookId) {
        List<Author> authors = authorRepository.findAll();
        Optional<Book> bookById = bookRepository.findBookById(bookId);
        bookById.ifPresent(book -> authors.removeAll(List.of(book.getAuthor())));

        return authors.stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    private List<GenreDto> findGenresNotInBook(long bookId) {
        List<Genre> genres = genreRepository.findAll();
        Optional<Book> bookById = bookRepository.findBookById(bookId);
        bookById.ifPresent(book -> genres.removeAll(book.getGenres()));

        return genres.stream()
                .map(GenreMapper::toDto)
                .toList();
    }
}
