import { Component  } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthorEditorComponent } from '@components/author-editor/author-editor.component';
import { AuthorsService } from '@services/authors-service/authors.service';
import { AuthorDto } from '@dto/author-dto';

@Component({
    selector: 'app-authors',
    standalone: true,
    imports: [CommonModule, AuthorEditorComponent],
    templateUrl: './authors.component.html',
    styleUrl: './authors.component.scss',
    providers: [
        { provide: AuthorsService, useClass: AuthorsService },
        { provide: AuthorEditorComponent, useClass: AuthorEditorComponent },
    ]
})
export class AuthorsComponent {
    authors: AuthorDto[] = [];
    author: AuthorDto = new AuthorDto(0, "");
    error: unknown = "";

    /**
     * Inject Service and init list authors
     * @param authorsService - REST server
     */
    constructor(private authorsService: AuthorsService) {

        this.findAll();
    }

    /**
     * Get Authors via Service by REST
     */
    private findAll(): void {
        const resp = this.authorsService.findAll().subscribe((res: AuthorDto[]) => {
            this.authors = res;
        });
    }

    /**
     * Delete Author via Service by REST
     */
    onClickDelete(authorDto: AuthorDto): void  {
        try {
            const res = this.authorsService.delete(authorDto).subscribe((authorRes: AuthorDto) => {
                this.author = authorRes;
                const index = this.authors.findIndex((a: AuthorDto) => {
                    return a.id === authorRes.id;
                });
                this.authors.splice(index, 1);
                this.error = `deleted: ${authorRes.fullName}`;
            });
        }
        catch(error) {
            this.error = error;
        }
    }

    /**
     * Fill Author Editor model
     * @param author
     */
    onRowClick(author: AuthorDto): void  {
        this.author = author;
    }

    /**
     * Event add author handler
     * Receive Event(addAuthorEvent) form Child(AuthorEditorComponent) to Parent(AuthorsComponent)
     * @param author
     */
    receiveAddAuthorEvent(author: AuthorDto): void {
        this.authors.push(author);
    }

    /**
     * Event save author handler
     * Receive Event(saveAuthorEvent) form child (AuthorEditorComponent) to Parent(AuthorsComponent)
     * @param author
     */
    receiveSaveAuthorEvent(author: AuthorDto): void {
        const index = this.authors.findIndex((a) => a.id === author.id );
        this.authors[index] = author;
    }
}
