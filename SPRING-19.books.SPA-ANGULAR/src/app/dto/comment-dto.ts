import { BookDto } from "./book-dto";

export class CommentDto {
    constructor(
        public id: number,
        public description: string,
        public book: BookDto
      ) {}
}

