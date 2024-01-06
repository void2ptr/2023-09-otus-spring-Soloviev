//package ru.otus.hw.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//import ru.otus.hw.model.Authors;
//import ru.otus.hw.repository.AuthorsRepository;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.web.reactive.function.BodyInserters.fromValue;
//import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
//import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//
//@Configuration
//public class AuthorsRouteConfig {
//
//    @Bean
//    public RouterFunction<ServerResponse> composedRoutes(AuthorsRepository repository) {
//        return route()
//
//                .GET("/api/v1/authors/{id}",  accept(APPLICATION_JSON),
//                        request -> repository.findAuthorById(Long.parseLong(request.pathVariable("id")))
//                                .flatMap(author -> ok().contentType(APPLICATION_JSON).body(fromValue(author)))
//                                .switchIfEmpty(notFound().build())
//                )
//                .GET("/api/v1/authors", accept(APPLICATION_JSON), new AuthorHandler(repository)::list)
//                .build();
//    }
//
//    // Это пример хэндлера, который даже не бин
//    static class AuthorHandler {
//
//        private final AuthorsRepository repository;
//
//        AuthorHandler(AuthorsRepository repository) {
//            this.repository = repository;
//        }
//
//        Mono<ServerResponse> list(ServerRequest request) {
//            // Обратите внимание на пример другого порядка создания response от Flux
//            return ok().contentType(APPLICATION_JSON).body(repository.findAll(), Authors.class);
//        }
//    }
//
//}
