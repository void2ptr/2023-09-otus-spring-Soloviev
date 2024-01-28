package ru.otus.hw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.config.security.SecurityConfig;
import ru.otus.hw.controller.AuthorsController;
import ru.otus.hw.data.UrlAndUser;
import ru.otus.hw.data.UrlGetArgumentsProvider;
import ru.otus.hw.data.UrlPostArgumentsProvider;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultHandlers.exportTestSecurityContext;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@DisplayName("проверка Get на безопасность")
@WebMvcTest(AuthorsController.class)
//@Import({SecurityConfig.class})

//@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
//@WebAppConfiguration

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ControllersSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("GET: должен пройти проверку подлинности (authenticated)")
    @ParameterizedTest
    @ArgumentsSource(UrlGetArgumentsProvider.class)
    void checkSecurityForGetResource(UrlAndUser urlAndUser) throws Exception  {

        System.out.printf("----%s:%s@ %s\n", urlAndUser.user().getUsername(),
                urlAndUser.user().getPassword(), urlAndUser.url());

        // test
       mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                    .with(user(urlAndUser.user()))
                )
                .andDo(exportTestSecurityContext())
                .andDo(print())
                .andExpect(authenticated());
    }

    @DisplayName("POST: должен пройти проверку подлинности (authenticated)")
    @ParameterizedTest
    @ArgumentsSource(UrlPostArgumentsProvider.class)
    void checkSecurityForPostResource(UrlAndUser urlAndUser) throws Exception  {

        System.out.printf("----%s:%s@ %s\n", urlAndUser.user().getUsername(),
                urlAndUser.user().getPassword(), urlAndUser.url());

        // test
        mockMvc.perform(post(urlAndUser.url())
                        .with(csrf())
                        .with(user(urlAndUser.user()))
                )
                .andDo(exportTestSecurityContext())
                .andDo(print())
                .andExpect(authenticated());
    }

    @DisplayName("GET: не должен пройти проверку подлинности (unauthenticated)")
    @ParameterizedTest
    @ArgumentsSource(UrlGetArgumentsProvider.class)
    void checkAnonymousGetResource(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(get(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(unauthenticated());
    }

    @DisplayName("POST: не должен пройти проверку подлинности (unauthenticated)")
    @ParameterizedTest
    @ArgumentsSource(UrlPostArgumentsProvider.class)
    void checkAnonymousPostResource(UrlAndUser urlAndUser) throws Exception  {
        // test
        mockMvc.perform(post(urlAndUser.url())
                        .with(csrf())
                        .with(anonymous()))
                .andExpect(unauthenticated());
    }

}