//package ru.otus.hw.config;
//
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//
//
//@Configuration
//@EnableR2dbcRepositories
//public class R2DbcConfiguration extends AbstractR2dbcConfiguration {
//    private final ConnectionFactory connectionFactory;
//
//    R2DbcConfiguration( ConnectionFactory connectionFactory ) {
//        this.connectionFactory = connectionFactory;
//    }
//    @Override
//    public ConnectionFactory connectionFactory() {
//        return this.connectionFactory;
//    }
//}
