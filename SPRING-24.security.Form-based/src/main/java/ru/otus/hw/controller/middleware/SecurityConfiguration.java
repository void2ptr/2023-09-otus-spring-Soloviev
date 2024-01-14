package ru.otus.hw.controller.middleware;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.service.UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    public static final String[] ENDPOINTS_WHITELIST = {"/","/index/**","/static/**", "/css/**"};
    private final String REMEMBER_ME_KEY = "MY_SECRET_KEY";
    private final int REMEMBER_ME_SEC = 60 * 10; // token validity seconds

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests( ( authorize ) -> authorize
//                        .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
//                        .requestMatchers( "/**" ).authenticated()

                        .requestMatchers( HttpMethod.POST, "/books/**").authenticated()
                        .requestMatchers( HttpMethod.GET, "/books/**").authenticated()
//                        .requestMatchers( "/books/**").authenticated()
//
//                        .requestMatchers( "/authors/**" ).authenticated()
//                        .requestMatchers( "/genres/**").authenticated()
//                        .requestMatchers("/comments/**").authenticated()
//                        .requestMatchers("/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().denyAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .rememberMe((RememberMeConfigurer<HttpSecurity> httpSecurity) -> httpSecurity
                        .key(REMEMBER_ME_KEY)
                        .tokenValiditySeconds(REMEMBER_ME_SEC))
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
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

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var users = this.userService.findAll();
        return new InMemoryUserDetailsManager( users );
    }

}
