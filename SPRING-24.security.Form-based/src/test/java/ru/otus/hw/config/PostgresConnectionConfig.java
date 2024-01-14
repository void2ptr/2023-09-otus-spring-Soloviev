//package ru.otus.hw.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.PersistenceUnit;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//
//@Configuration
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableConfigurationProperties
//@ConfigurationProperties(prefix = "spring")
//public class PostgresConnectionConfig {
//
//    @Value("${spring.datasource.driverClassName.url:org.postgresql.Driver}")
//    String driverClassName;
//    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5430/demoDB}")
//    String url;
//    @Value("${spring.datasource.username:sa}")
//    String username;
//    @Value("${spring.datasource.password:pwd}")
//    String password;
//
////    @Bean
////    public Connection connectionFactory() throws SQLException {
////        Connection conn;
////        try (conn = DriverManager.getConnection(url, username, password)) {
////
////            if (conn != null) {
////                System.out.println("Connected to the database!");
////            } else {
////                System.out.println("Failed to make connection!");
////            }
////
////        } catch (SQLException e) {
////            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return DriverManager.getConnection(url, username, password);
////    }
//
//    //    @Bean
////    public ConnectionFactory connectionFactory() {
//        //     url: jdbc:postgresql://localhost:5430/demoDB
////        return new PostgresqlConnectionFactory( PostgresqlConnectionConfiguration.builder()
////                        .host(host)
////                        .port(port)
////                        .username(username)
////                        .password(password)
////                        .database(database)
////                        .build());
////    }
//
//    public DataSource getDataSource() {
//        return DataSourceBuilder.create()
//                .driverClassName(driverClassName)
//                .url(url)
//                .username(username)
//                .password(password)
//                .build();
//
//    }
//
//
//    @PersistenceUnit
//    public EntityManagerFactory emf;
//}
