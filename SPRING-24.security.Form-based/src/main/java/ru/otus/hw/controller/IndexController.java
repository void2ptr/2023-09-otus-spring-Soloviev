package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping({"/", "/index", "/index.html"})
    public String indexPage(Model model) {
        this.logAuthentication();
        return "/index";
    }

    @GetMapping({"/error"})
    public String errorPage(BindingResult result, Model model) {
        result.reject("errorCode1", "Global Error Happened");
        result.rejectValue("newField", "Error 2 happened");
        model.addAttribute("error", "");
        this.logAuthentication();
        return "/error";
    }

    private void logAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication =  securityContext.getAuthentication();
        System.out.println(authentication.getPrincipal());
    }
}
