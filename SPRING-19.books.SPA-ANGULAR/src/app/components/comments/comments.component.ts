import { Component, Input, SimpleChange } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CommentsService} from '@services/comments-service/comments.service';
import { CommentDto } from '@dto/comment-dto';
import { BookDto } from '@dto/book-dto';
import { AuthorDto } from '@dto/author-dto';


@Component({
    selector: 'app-comments',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './comments.component.html',
    styleUrl: './comments.component.scss',
    providers: [
        { provide: CommentsService, useClass: CommentsService },
    ]
})
export class CommentsComponent {
    @Input() book: BookDto = new BookDto(0, "", new AuthorDto(0, ""), []);
    comments: CommentDto[] = [];
    comment: CommentDto = new CommentDto(0, "", new BookDto(0, "", new AuthorDto(0, ""), []));
    error: unknown = "";



    /**
     * Inject Service
     * @param commentsService
     */
    constructor(
        private commentsService: CommentsService
    ) {
    }

    /**
     * get a Comments from Service via REST
     */
    private findCommentsByBookById(bookId: number): void {
        try {
            const resp = this.commentsService.findCommentsByBookById(bookId).subscribe((comments: CommentDto[]) => {
                this.comments = comments;
            });
        } catch(error) {
            this.error = error;
        }
    }

    /**
     * Trigger on click event by parent component
     * @param changes
     * @returns
     */
    ngOnChanges(changes: { book: SimpleChange }) {
        // Extract changes to the input property by its name
        const book = { ...this.book, ...changes['book'] };

        if (book.id < 1) {
            return;
        }
        this.receiveClickBookEvent(book);
        this.comment.book = book;
    }

   /**
     * Insert New Comment via Service by REST
     */
   addRow(): void {
        const res = this.commentsService.create(this.comment).subscribe((res: CommentDto) => {
            this.comments.push(res);
        });
    }

    /**
     * Save a Comment via Service by REST
     * @param index - table row index
     */
    editRow(index: number) {
        this.getFreshModel(index);
        const res = this.commentsService.update(this.comment).subscribe((res: CommentDto) => {
            // this.genres.push({ id: res.id, name: res.name });
        });
    }

    /**
     * Delete a Comment via Service by REST
     * @param index - table row index
     */
    deleteRow(index: number) {
        const res = this.commentsService.delete(this.comments[index]).subscribe((res: CommentDto) => {
            this.comments.splice(index, 1);
        });
    }

    /**
     * Fill (ReFresh) model before send it via REST
     * @param index - table row index
     * @returns
     */
    private getFreshModel(index: number){
        const elem = document.querySelector(`[data-id="${this.comments[index].id}"]`) as HTMLInputElement;
        if (!elem) {
            return;
        }
        this.onKey(elem);
    }
    /**
     * Triggered when user edit comment
     * Fill the Genre model, after click on table.row
     * @param target
     */
    onKey(target: HTMLInputElement) {
        this.comment.id          = Number(target.dataset['id']);
        this.comment.description = target.value;
    }

    /**
     * Receive Event(clickBookEvent) form Parent(BooksComponent) to Child(CommentsComponent)
     * @param book
     */
    receiveClickBookEvent(book: BookDto): void {
        this.findCommentsByBookById(book.id);
    }

}
