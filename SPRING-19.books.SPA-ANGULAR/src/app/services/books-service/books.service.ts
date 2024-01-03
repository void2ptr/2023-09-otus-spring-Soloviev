import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, retry } from 'rxjs';

import { BookDto } from '@dto/book-dto';

@Injectable({
    providedIn: 'root'
})
export class BooksService {
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
     * Get Books via REST
     * @returns array of Books
     */
    public findAll(): Observable<BookDto[]> {
        let url = `${this.api}/books`;
        return this.http.get<BookDto[]>(url, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Get Book via REST by id
     * @param id - Book.id
     * @returns
     */
    public findById(id: number): Observable<BookDto> {
        let url = `${this.api}/books/${id}`;
        return this.http.get<BookDto>(url, {
            headers: this.headers,
            params: new HttpParams().set('id', id)
        }).pipe(
            retry(0),
        );
    }

    /**
     * Insert Book via REST
     * @param book
     * @returns
     */
    public create(book: BookDto): Observable<BookDto> {
        let url = `${this.api}/books`;
        const body = JSON.stringify(book);
        return this.http.post<BookDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Update Book via REST
     * @param book
     * @returns
     */
    public update(book: BookDto): Observable<BookDto> {
        let url = `${this.api}/books/${book.id | 0}`;
        const body = JSON.stringify(book);
        return this.http.put<BookDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
        );
    }

    /**
     * Delete Book via REST
     * @param book
     * @returns
     */
    public delete(book: BookDto): Observable<BookDto> {
        let url = `${this.api}/books/${book.id | 0}`;
        const body = JSON.stringify(book);

        return this.http.delete<BookDto>(url, {
            headers: this.headers//,
            // body: body
        }).pipe(
            retry(0),
        );
    }
}
