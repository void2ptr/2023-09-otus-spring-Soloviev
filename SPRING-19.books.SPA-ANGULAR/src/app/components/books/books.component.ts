import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommentsComponent } from '@components/comments/comments.component';
import { BookEditorComponent } from '@components/book-editor/book-editor.component';

import { BooksService } from '@services/books-service/books.service';
import { BookDto } from '@dto/book-dto';
import { AuthorDto } from '@dto/author-dto';
import { GenreDto } from '@dto/genre-dto';


@Component({
    selector: 'app-books',
    standalone: true,
    imports: [CommonModule, BookEditorComponent, CommentsComponent],
    templateUrl: './books.component.html',
    styleUrl: './books.component.scss',
    providers: [
        { provide: BooksService, useClass: BooksService },
        { provide: BookEditorComponent, useClass: BookEditorComponent }
    ]
})
export class BooksComponent {
    books: BookDto[] = [];
    book: BookDto = new BookDto(0, "", new AuthorDto(0, ""), [new GenreDto(0, "")]);
    error: unknown = "";

    /**
     * Inject BooksService
     * init Books list
     */
    constructor(
        private booksService: BooksService
    ) {
        this.findAllBook();
    }

    /**
     * get Books from the Service via REST
     */
    private findAllBook(): void {
        try {
            const resp = this.booksService.findAll().subscribe((res: BookDto[]) => {
                this.books = res;
            });
        } catch(error) {
            this.error = error;
        }
    }

    /**
     * Deleting a book through the REST service, and then deleting a line from the page
     * @param book
     */
    onClickDelete(book: BookDto): void {
        try {
            const resp = this.booksService.delete(book).subscribe((res: BookDto) => {
                this.book = res;
                const index = this.books.findIndex((book: BookDto) => {
                    return book.id === res.id;
                });
                this.books.splice(index, 1);
                this.error = `deleted: ${res.title}`;

            });
        } catch(error) {
            this.error = error;
        }
    }

    /**
     * Click on book row.cell - send selected book to Child component via update model
     * @param book
     */
    onCellClick(book: BookDto): void  {
        this.book = book;
    }

    /**
     * Event add book handler
     * Receive Event(addBookEvent) form Child(BookEditorComponent) to Parent(BooksComponent)
     * - from Child to Parent
     * @param book
     */
    receiveAddBookEvent(book: BookDto): void {
        this.books.push(book);
    }

    /**
     * Event save book handler
     * Receive Event(saveBookEvent) form child (BookEditorComponent) to Parent(BooksComponent)
     * @param book
     */
    receiveSaveBookEvent(book: BookDto): void {
        const index = this.books.findIndex((b) => b.id === book.id );
        this.books[index] = book;
    }
}
