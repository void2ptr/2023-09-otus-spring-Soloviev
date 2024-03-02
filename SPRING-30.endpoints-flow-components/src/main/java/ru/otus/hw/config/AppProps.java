package ru.otus.hw.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AppProps {

    @Value("${application.data.path-input}")
    private String pathInput;

    @Value("${application.data.path-output}")
    private String pathOutput;
}
