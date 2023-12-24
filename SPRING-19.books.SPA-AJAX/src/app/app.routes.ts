import { Routes } from '@angular/router';

import { AuthorsComponent } from './components/authors/authors.component';
import { BooksComponent } from './components/books/books.component';
import { CommentsComponent } from './components/comments/comments.component';
import { GenresComponent } from './components/genres/genres.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';


export const routes: Routes = [
  { path: 'app-authors', component: AuthorsComponent },
  { path: 'app-books', component: BooksComponent },
  { path: 'app-comments', component: CommentsComponent },
  { path: 'app-genres', component: GenresComponent },
  { path: '', redirectTo: '/app-authors', pathMatch: 'full'},
  { path: '**', component: PageNotFoundComponent },
];
