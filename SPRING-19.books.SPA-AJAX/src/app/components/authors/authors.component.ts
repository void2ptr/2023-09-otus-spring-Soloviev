import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, of } from 'rxjs';

import { AuthorsService } from './../../services/author/authors.service';
import { Author } from '../../dto/author';

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
  authors: Author[] = [];

  constructor(private authorsService: AuthorsService) {

      const resp = authorsService.getAll().subscribe((res: Author[]) => {
          this.authors = res;
      });

      // console.log( resp );

    // getAuthors();
  }

  // getAuthors(): Observable<Author[]>  {

  //     debugger;
  //     this.authors = this.http.get<Author[]>("/api/products").pipe(shareReplay(1));
  //     return this.authors;
  // }

}
