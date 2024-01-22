package ru.otus.hw.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.service.UserService;

import java.util.Date;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String[] ENDPOINTS_WHITELIST = {"/","/index/**","/static/**", "/css/**"};

    private static final String REMEMBER_ME_KEY = "MY_SECRET_KEY" + new Date().getTime(); // change it after restart !

    private static final int REMEMBER_ME_SEC = 60 * 10; // token validity seconds

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                        .requestMatchers("/*/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/books/**").authenticated()
                        .requestMatchers("/books/**/comments/**").authenticated()
                        .requestMatchers("/authors/**").authenticated()
                        .requestMatchers("/genres/**").authenticated()
                        .anyRequest().denyAll())
//                .addFilterAfter(new MyOwnFilter(), ConcurrentSessionFilter.class)
//                .anonymous(Customizer.withDefaults())
                .rememberMe((RememberMeConfigurer<HttpSecurity> httpSecurity) -> httpSecurity
                        .key(REMEMBER_ME_KEY).tokenValiditySeconds(REMEMBER_ME_SEC))
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
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

    @Bean(name = "userDetailsService")
    public UserService userDetailsService() {
        return userService;
    }

}
