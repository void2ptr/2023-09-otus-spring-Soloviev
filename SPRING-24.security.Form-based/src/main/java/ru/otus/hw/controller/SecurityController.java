package ru.otus.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class SecurityController {

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @GetMapping({"/login"})
    public String login(Model model) {
        return "/login";
    }

    @GetMapping({"/logout"})
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/login";
    }

}
