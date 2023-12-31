import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, retry } from 'rxjs';
import { CommentDto } from '@app/dto/comment-dto';

@Injectable({
    providedIn: 'root'
})
export class CommentsService {
    private api = '/api/v1';
    private headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    });

    constructor(private http: HttpClient) { }

    public findCommentsByBookById(bookId: number): Observable<CommentDto[]> {
      let url = `${this.api}/book/${bookId | 0}/comment`;
      return this.http.get<CommentDto[]>(url, { headers: this.headers }).pipe(
          retry(0)
      );
  }

  public create(comment: CommentDto): Observable<CommentDto> {
      let url = `${this.api}/book/${comment.book.id | 0}/comment/add`;
      const body = JSON.stringify(comment);
      return this.http.post<CommentDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
      );
  }

  public update(comment: CommentDto): Observable<CommentDto> {
      let url = `${this.api}/book/${comment.book.id | 0}/comment/${comment.id | 0}/edit`;
      const body = JSON.stringify(comment);
      return this.http.post<CommentDto>(url, body, { headers: this.headers }).pipe(
            retry(0),
     );
  }

  public delete(comment: CommentDto): Observable<CommentDto> {
      let url = `${this.api}/book/${comment.book.id | 0}/comment/${comment.id | 0}/delete`;
      // const body = JSON.stringify(comment);

      return this.http.delete<CommentDto>(url, {
          headers: this.headers
      }).pipe(
            retry(0),
      );
  }

}
