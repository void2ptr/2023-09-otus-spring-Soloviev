package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@AllArgsConstructor
@Getter
@Setter
public class GenreMapper {

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }

    public static Genre toGenre(GenreDto genreDto) {
        return new Genre(
                genreDto.getId(),
                genreDto.getName()
        );
    }
}
