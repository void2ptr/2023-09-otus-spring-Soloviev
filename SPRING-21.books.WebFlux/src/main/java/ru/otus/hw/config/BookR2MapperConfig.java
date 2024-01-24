package ru.otus.hw.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.mapper.BookR2Mapper;

@Configuration
public class BookR2MapperConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BookR2Mapper getBookR2Mapper(ObjectMapper objectMapper) {
        return new BookR2Mapper(objectMapper);
    }
}
