import { Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

import { AuthorsComponent } from './components/authors/authors.component';
import { BooksComponent } from './components/books/books.component';
import { CommentsComponent } from './components/comments/comments.component';
import { GenresComponent } from './components/genres/genres.component';
import { environment } from './../environments/environment';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive,
            AuthorsComponent, BooksComponent, CommentsComponent, GenresComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  // providers: [AuthorsComponent]
})
export class AppComponent {
    title = 'Books-SPA';
    constructor() {
       console.log(environment.production); // Logs false for development environment
    }
}
