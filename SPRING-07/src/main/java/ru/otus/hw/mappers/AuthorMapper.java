package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Author;
import ru.otus.hw.dto.AuthorDto;

@AllArgsConstructor
@Getter
@Setter
public class AuthorMapper {

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }

    public static Author toAuthor(AuthorDto authorDto) {
        return new Author(
                authorDto.getId(),
                authorDto.getFullName()
        );
    }
}
