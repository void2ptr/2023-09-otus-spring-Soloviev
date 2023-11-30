package ru.otus.hw.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "genres")
public class Genre {
    @Id
    private String id;
    @Indexed
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
