import { Component  } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthorsService } from '@services/authors-service/authors.service';
import { AuthorDto } from '@dto/author-dto';


@Component({
    selector: 'app-authors',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './authors.component.html',
    styleUrl: './authors.component.scss',
    providers: [
        { provide: AuthorsService, useClass: AuthorsService }
    ]
})
export class AuthorsComponent {
    authors: AuthorDto[] = [];
    authorDto: AuthorDto = new AuthorDto(0, "");
    error: unknown = "";

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
     * Add New Author via Service by REST
     */
    onClickAdd(): void  {
        this.getAuthor();

        const res = this.authorsService.create(this.authorDto).subscribe((res: AuthorDto) => {
            this.authorDto = res;
            this.authors.push(this.authorDto);
            this.error = `added: ${res.fullName}`;
        });
    }

    /**
     * Save Author via Service by REST
     */
    onClickEdit(): void  {
        this.getAuthor();
        const res = this.authorsService.update(this.authorDto).subscribe((res: AuthorDto) => {
            this.authorDto = res;
            const index = this.authors.findIndex((a: AuthorDto) => {
                return a.id === res.id;
            });
            this.authors[index] = res;
            this.error = `save: ${res.fullName}`;
        });
    }

    /**
     * Delete Author via Service by REST
     */
    onClickDelete(authorDto: AuthorDto): void  {
        try {
            const res = this.authorsService.delete(authorDto).subscribe((authorRes: AuthorDto) => {
                this.authorDto = authorRes;
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
    onClick(author: AuthorDto): void  {
        this.authorDto = author;
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
        this.authorDto = new AuthorDto(Number(elemAuthorId.textContent), elemAuthorName.value);
    }
}
