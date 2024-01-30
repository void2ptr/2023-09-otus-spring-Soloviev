package ru.otus.hw.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.mapper.BookR2ExistMapper;
import ru.otus.hw.mapper.BookR2RowMapper;

@Configuration
public class BookR2MapperConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BookR2RowMapper getBookR2RowMapper(ObjectMapper objectMapper) {
        return new BookR2RowMapper(objectMapper);
    }

    @Bean
    public BookR2ExistMapper getBookR2ExistMapper() {
        return new BookR2ExistMapper();
    }

}
