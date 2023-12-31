import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, retry } from 'rxjs';

import { GenreDto } from '@dto/genre-dto';

@Injectable({
  providedIn: 'root'
})
export class GenresService {
    private api = '/api/v1';
    private headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    });

    /**
     * Inject HttpClient
     * @param http
     */
    constructor(private http: HttpClient) { }

    /**
     * Get Genres via REST
     * @returns array of Genres
     */
    public findAll(): Observable<GenreDto[]> {
        let url = `${this.api}/genre`;
        return this.http.get<GenreDto[]>(url, { headers: this.headers }).pipe(
            retry(0), // retry a failed request up to 3 times
        );
    }

    /**
     * Insert Genre via REST
     * @param genre
     * @returns
     */
    public create(genre: GenreDto): Observable<GenreDto> {
        let url = `${this.api}/genre/add`;
        const body = JSON.stringify(genre);
        return this.http.post<GenreDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Update Genre via REST
     * @param genre
     * @returns
     */
    public update(genre: GenreDto): Observable<GenreDto> {
        let url = `${this.api}/genre/${genre.id | 0}/edit`;
        const body = JSON.stringify(genre);
        return this.http.post<GenreDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
       );
    }

    /**
     * Delete Genre via REST
     * @param genre
     * @returns
     */
    public delete(genre: GenreDto): Observable<GenreDto> {
        let url = `${this.api}/genre/${genre.id | 0}/delete`;
        const body = JSON.stringify(genre);

        return this.http.delete<GenreDto>(url, {
            headers: this.headers,
            body: body
        }).pipe(
            retry(0),
        );
    }
}
