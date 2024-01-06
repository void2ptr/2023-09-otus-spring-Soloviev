//package ru.otus.hw.config.del;
//
//import io.r2dbc.h2.H2ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactory;
//import org.flywaydb.core.Flyway;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableConfigurationProperties
//public class H2ConnectionConfig {
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return H2ConnectionFactory.inMemory("r2dbc:h2:mem:testdb");
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactoryJdbc() {
//        return H2ConnectionFactory.inMemory("jdbc:h2:mem:testdb");
//    }
//
//    public Flyway flyway() {
//        System.out.println("####### Using H2 in mem Flyway connection");
//        return new Flyway(Flyway.configure()
//                .baselineOnMigrate(true)
//                .dataSource(
//                        "jdbc:h2:mem:testdb",
//                        "",
//                        "")
//        );
//    }
//}
