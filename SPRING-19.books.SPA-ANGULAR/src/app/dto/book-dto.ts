import { AuthorDto } from '@dto/author-dto';
import { GenreDto } from '@dto/genre-dto';

export class BookDto {
  constructor(
      public id: number,
      public title: string,
      public author: AuthorDto,
      public genres: GenreDto[]
    ) {}
}
