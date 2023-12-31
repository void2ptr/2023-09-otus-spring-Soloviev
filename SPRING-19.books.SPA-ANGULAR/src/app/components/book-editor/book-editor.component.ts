import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, NgForm } from '@angular/forms';

import { BooksService } from '@services/books-service/books.service';
import { BookDto } from '@dto/book-dto';

import { AuthorsService } from '@services/authors-service/authors.service';
import { AuthorDto } from '@dto/author-dto';

import { GenresService } from '@services/genres-service/genres.service';
import { GenreDto } from '@dto/genre-dto';


@Component({
    selector: 'app-book-editor',
    standalone: true,
    imports: [CommonModule, BrowserModule, FormsModule],
    templateUrl: './book-editor.component.html',
    styleUrl: './book-editor.component.scss',
    providers: [
        { provide: BooksService, useClass: BooksService },
        { provide: AuthorsService, useClass: AuthorsService },
        { provide: GenresService, useClass: GenresService },
    ]
})
export class BookEditorComponent {
    @Input() book: BookDto = new BookDto(0, "", new AuthorDto(0, ""), []);
    @Input() authors: AuthorDto[] = [];
    @Input() genres: GenreDto[] = [];

    @Output() addBookEvent = new EventEmitter();
    @Output() saveBookEvent = new EventEmitter();

    error: unknown = "";

    /**
     * Constructor Init Services, Authors and Genres
     * @param booksService
     * @param authorsService
     * @param genresService
     */
    constructor(
        private booksService: BooksService,
        private authorsService: AuthorsService,
        private genresService: GenresService
    ) {
        this.findAllAuthors();
        this.findAllGenres();
    }

    /**
     * Get Authors via Service by REST
     */
    private findAllAuthors(): void {
        const resp = this.authorsService.findAll().subscribe((res: AuthorDto[]) => {
            this.authors = res;
        });
    }

    /**
     * Get Genres via Service by REST
     */
    private findAllGenres(): void {
        const resp = this.genresService.findAll().subscribe((res: GenreDto[]) => {
          this.genres = res;
        });
    }

    /**
     * Listen Event(ClickRowEvent) handler
     */
    receiveClickRowEvent(book: BookDto): void  {
        this.book = book;
    }

    /**
     * Add Book via Service by REST
     * @param form
     */
    onClickAdd(form: NgForm): void {
        const bookForm = { ...this.book, ...form.value };

        const res = this.booksService.create(bookForm).subscribe((res: BookDto) => {
            this.book = res;
            this.addBookEvent.emit(res);
            this.error = `added: ${res.title}`;
        });
    }

    /**
     * Update Book via Service by REST
     * @param form
     */
    onClickSave(form: NgForm): void {
        const bookForm = { ...this.book, ...form.value};

        const res = this.booksService.update(bookForm).subscribe((res: BookDto) => {
            this.book = res;
            this.saveBookEvent.emit(res);
            this.error = `saved: ${res.title}`;
        });
    }
}
