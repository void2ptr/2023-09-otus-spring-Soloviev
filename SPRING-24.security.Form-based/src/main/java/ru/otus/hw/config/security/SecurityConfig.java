package ru.otus.hw.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.repository.UserRepository;
import ru.otus.hw.service.UserService;
import ru.otus.hw.service.UserServiceImpl;

import java.util.Date;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String[] ENDPOINTS_WHITELIST = {"/","/index/**","/static/**", "/css/**"};

    private static final String REMEMBER_ME_KEY = "MY_SECRET_KEY_" + new Date().getTime(); // change it after restart !

    private static final int REMEMBER_ME_SECONDS = 60 * 10; // token validity seconds

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(withDefaults())
                .cors(withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                        .requestMatchers("/*", "/*/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/books/**").authenticated()
                        .requestMatchers("/books/**/comments/**").authenticated()
                        .requestMatchers("/authors/**").authenticated()
                        .requestMatchers("/genres/**").authenticated()
                        .anyRequest().denyAll())
//                .addFilterAfter(new MyOwnFilter(), ConcurrentSessionFilter.class)
//                .anonymous(Customizer.withDefaults())
                .rememberMe().key(REMEMBER_ME_KEY).tokenValiditySeconds(REMEMBER_ME_SECONDS).and()
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true))
                .formLogin();
        return http.build();
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delegatingPasswordEncoder;
    }

    @Bean(name = "userDetailsService") // move to: service/UserService
    public UserService userDetailsService(@Lazy UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

}
