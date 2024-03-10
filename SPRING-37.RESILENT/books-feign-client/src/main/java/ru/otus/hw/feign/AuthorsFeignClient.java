package ru.otus.hw.feign;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.AuthorDto;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;


import java.util.List;

@FeignClient(name = "${feign.client.name:authorsFeignClient}", url = "${feign.client.url}")
@Cacheable("authors")
@Retryable(retryFor = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
public interface AuthorsFeignClient {

    @GetMapping(value = "/api/v1/authors", produces = "application/json")
    List<AuthorDto> findAll();

    @GetMapping(value = "/api/v1/authors/{id}", produces = "application/json")
    AuthorDto findById(@PathVariable("id") Long id);

    @PostMapping(value = "/api/v1/authors", produces = "application/json")
    AuthorDto insertAction(@RequestBody AuthorDto authorDto);

    @PutMapping(value = "/api/v1/authors", produces = "application/json")
    AuthorDto updateAction(@RequestBody AuthorDto authorDto);

    @DeleteMapping(value = "/api/v1/authors/{id}", produces = "application/json")
    AuthorDto deleteAction(@PathVariable("id") long id);

}
