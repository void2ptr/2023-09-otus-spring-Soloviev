package ru.otus.hw.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AppProps {

    @Value("${application.data.path}")
    private String path;

}
