//package ru.otus.hw.config;
//
//import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
//import io.r2dbc.postgresql.PostgresqlConnectionFactory;
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties(prefix = "spring.r2dbc")
//public class PostgresConnectionConfig {
//    @Value("${spring.r2dbc.host:localhost}")
//    String host;
//    @Value("${spring.r2dbc.port:5430}")
//    int port;
//    @Value("${spring.r2dbc.database:demoDB}")
//    String database;
//    @Value("${spring.r2dbc.username:sa}")
//    String username;
//    @Value("${spring.r2dbc.password:pwd}")
//    String password;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return new PostgresqlConnectionFactory( PostgresqlConnectionConfiguration.builder()
//                        .host(host)
//                        .port(port)
//                        .username(username)
//                        .password(password)
//                        .database(database)
//                        .build());
//    }
//
//}
