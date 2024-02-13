package ru.otus.hw.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.security.SecureRandom;
import java.util.Optional;


@Component
public class AuthorHealthIndicator implements HealthIndicator {
    private final SecureRandom random = new SecureRandom();

    private final AuthorRepository authorRepository;

    public AuthorHealthIndicator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Health health() {
        long count = authorRepository.count();
        long randAuthorId = random.nextLong(count);
        Optional<Author> authorOpt = authorRepository.findAuthorById(randAuthorId);
        if (authorOpt.isPresent()) {
            return Health.up()
                    .status(Status.UP)
                    .withDetail("author", authorOpt.get().getFullName())
                    .build();
        }
        return Health.down()
                .status(Status.DOWN)
                .withDetail("author", "all authors dead %d".formatted(randAuthorId))
                .build();
    }
}
