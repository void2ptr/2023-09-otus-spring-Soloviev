import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GenresService } from '@services/genres-service/genres.service';
import { GenreDto } from '@dto/genre-dto';

@Component({
    selector: 'app-genres',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './genres.component.html',
    styleUrl: './genres.component.scss',
    providers: [
        { provide: GenresService, useClass: GenresService }
    ]
})
export class GenresComponent {
    genres: GenreDto[] = [];
    genre: GenreDto = new GenreDto(0, '');

    /**
     * Constructor init Service and Genres
     * @param genresService
     */
    constructor(private genresService: GenresService) {
        this.findAll();
    }

    /**
     * Find All Genres via Service by REST
     */
    private findAll(): void {
        const resp = this.genresService.findAll().subscribe((res: GenreDto[]) => {
            this.genres = res;
        });
    }

    /**
     * Insert New Genre via Service by REST
     */
    addRow(): void {
        const res = this.genresService.create(this.genre).subscribe((res: GenreDto) => {
            this.genres.push(res);
        });
    }

    /**
     * Save Genre via Service by REST
     * @param index - table row index
     */
    editRow(index: number) {
        this.getFreshModel(index);
        const res = this.genresService.update(this.genre).subscribe((res: GenreDto) => {
            // this.genres.push({ id: res.id, name: res.name });
        });
    }

    /**
     * Delete Genre via Service by REST
     * @param index - table row index
     */
    deleteRow(index: number) {
      this.getFreshModel(index);
        const res = this.genresService.delete(this.genre).subscribe((res: GenreDto) => {
            this.genres.splice(index, 1);
        });
    }

    /**
     * Fill (ReFresh) Genre model before send it via REST
     * @param index - table row index
     * @returns
     */
    private getFreshModel(index: number){
        const elem = document.querySelector(`[data-id="${this.genres[index].id}"]`) as HTMLInputElement;
        if (!elem) {
            return;
        }
        this.onKey(elem);
    }

    /**
     * Fill Genre model after click on table.row
     * @param target
     */
    onKey(target: HTMLInputElement) {
        this.genre.id   = Number(target.dataset['id']);
        this.genre.name = target.value;
    }
}
