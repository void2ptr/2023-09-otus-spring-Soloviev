import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'

import { AuthorsService } from './../../services/author/authors.service';

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
  authors: { id: string; fullName: string; }[] = [];

  constructor(private authorsService: AuthorsService) {
     this.authors = authorsService.getAll();
  }

}
