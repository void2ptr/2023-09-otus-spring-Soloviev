package ru.otus.hw.config.props;


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
public class InsectsRepositoryProps {

    @Value("${insects.data.path-input}")
    private String pathInput;

    @Value("${insects.data.path-output}")
    private String pathOutput;
}
