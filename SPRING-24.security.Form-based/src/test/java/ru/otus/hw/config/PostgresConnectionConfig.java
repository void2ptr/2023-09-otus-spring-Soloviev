//package ru.otus.hw.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties(prefix = "spring")
//public class PostgresConnectionConfig {
//
//    @Value("${spring.datasource.driverClassName.url:org.postgresql.Driver}")
//    String driverClassName;
//
//    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5430/demoDB}")
//    String url;
//    @Value("${spring.datasource.username:sa}")
//    String username;
//    @Value("${spring.datasource.password:pwd}")
//    String password;
//
////    @Bean
////    public ConnectionFactory connectionFactory() {
////        return new PostgresqlConnectionFactory( PostgresqlConnectionConfiguration.builder()
////                        .host(host)
////                        .port(port)
////                        .username(username)
////                        .password(password)
////                        .database(database)
////                        .build());
////    }
//
//
//    @Bean
//    public DataSource getDataSource() {
//        return DataSourceBuilder.create()
//                .driverClassName(driverClassName)
//                .url(url)
//                .username(username)
//                .password(password)
//                .build();
//    }
//}
