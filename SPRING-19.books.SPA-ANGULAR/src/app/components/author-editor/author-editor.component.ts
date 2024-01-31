import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthorsService } from '@services/authors-service/authors.service';
import { AuthorDto } from '@dto/author-dto';


@Component({
  selector: 'app-author-editor',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './author-editor.component.html',
  styleUrl: './author-editor.component.scss'
})
export class AuthorEditorComponent {
    @Input() author: AuthorDto = new AuthorDto(0, "");
    @Output() addAuthorEvent = new EventEmitter();
    @Output() saveAuthorEvent = new EventEmitter();
    error: unknown = "";

    /**
     * Inject Service
     * @param authorsService - REST server
     */
    constructor(private authorsService: AuthorsService) {
    }

    /**
     * Add New Author via Service by REST
     */
    onClickAdd(): void  {
        this.getAuthor();
        const res = this.authorsService.create(this.author).subscribe((res: AuthorDto) => {
            this.author = res;
            this.addAuthorEvent.emit(res);
            this.error = `added: ${res.fullName}`;
        });
    }

    /**
     * Save Author via Service by REST
     */
    onClickEdit(): void  {
        this.getAuthor();
        const res = this.authorsService.update(this.author).subscribe((res: AuthorDto) => {
            this.author = res;
            this.saveAuthorEvent.emit(res);
            this.error = `save: ${res.fullName}`;
        });
    }

    /**
     * Collect Author model
     * @returns
     */
    private getAuthor(): void {
        const elemAuthorId = document.querySelector('#author-id');
        if (! elemAuthorId) {
            return;
        }
        const elemAuthorName = (<HTMLInputElement>document.querySelector('#author-name'));
        if (! elemAuthorName) {
            return;
        }
        this.author = new AuthorDto(Number(elemAuthorId.textContent), elemAuthorName.value);
    }
}
