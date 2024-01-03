import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, retry } from 'rxjs';

import { AuthorDto } from '@dto/author-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthorsService {
    private api = '/api/v1';
    private headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    });

    /**
     * Inject HttpClient
     * @param http
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Get Authors via REST
     * @returns array of Authors
     */
    public findAll(): Observable<AuthorDto[]> {
        let url = `${this.api}/authors`;
        return this.http.get<AuthorDto[]>(url, { headers: this.headers }).pipe(
           retry(0),
        );
    }

    /**
     * Get Author via REST by id
     * @param id - Author,id
     * @returns Author
     */
    public findById(id: number): Observable<AuthorDto> {
        let url = `${this.api}/authors/${id}`;
        return this.http.get<AuthorDto>(url, {
            headers: this.headers,
            params: new HttpParams().set('id', id)
        }).pipe(
            retry(0),
        );
    }

    /**
     * Insert Author via REST
     * @param author
     * @returns
     */
    public create(author: AuthorDto): Observable<AuthorDto> {
        let url = `${this.api}/authors`;
        const body = JSON.stringify(author);
        return this.http.post<AuthorDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Update Author via REST
     * @param author
     * @returns
     */
    public update(author: AuthorDto): Observable<AuthorDto> {
        let url = `${this.api}/authors/${author.id | 0}`;
        const body = JSON.stringify(author);
        return this.http.put<AuthorDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Delete Author via REST
     * @param author
     * @returns
     */
    public delete(author: AuthorDto): Observable<AuthorDto> {
        let url = `${this.api}/authors/${author.id | 0}`;

        return this.http.delete<AuthorDto>(url, {
            headers: this.headers,
        }).pipe(
            retry(0),
        );
    }
}
