import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, retry, catchError, throwError } from 'rxjs';

import { Author } from '../../dto/author';

@Injectable({
  providedIn: 'root'
})
export class AuthorsService {
    private authorUrl = '/author';
    private headers = new HttpHeaders({
       'Content-Type': 'application/json',
       'Accept': 'application/json',
       'Access-Control-Allow-Headers': 'Content-Type',
       'Access-Control-Allow-Origin': '*',
       'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
       'mode': 'cors',
    });

    constructor(private http: HttpClient) {

    }

    public getAll(): Observable<Author[]> {
        return this.http.get<Author[]>(this.authorUrl, { headers: this.headers })
            .pipe(
                retry(3), // retry a failed request up to 3 times
                catchError(this.handleError) // then handle the error
            );
    }

    private handleError(error: HttpErrorResponse) {
        if (error.status === 0) {
          // A client-side or network error occurred. Handle it accordingly.
          console.error('An error occurred:', error.error);
        } else {
          // The backend returned an unsuccessful response code.
          // The response body may contain clues as to what went wrong.
          console.error(`Backend returned code ${error.status}, body was: `, error.error);
        }
        // Return an observable with a user-facing error message.
        return throwError(() => new Error('Something bad happened; please try again later.'));
    }
}
