package ru.otus.hw.config.prop;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
//@ConfigurationProperties(prefix = "app")
//@ConfigurationPropertiesScan
public class AppProps {

    @Value("${app.job-next}")
    private String jobNext;

    @Value("${app.current-item-count.author}")
    private String author;

    @Value("${app.current-item-count.book}")
    private String book;

    @Value("${app.current-item-count.comment}")
    private String comment;

    @Value("${app.current-item-count.genre}")
    private String genre;

}
