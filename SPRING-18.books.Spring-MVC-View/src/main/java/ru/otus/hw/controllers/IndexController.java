package ru.otus.hw.controllers;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
//@RequestMapping("/api/v1")
public class IndexController {

    @GetMapping({"/", "/api/v1", "/index", "/index.html"})
    public String indexPage(Model model) {
        return "/api/v1/index";
    }

    // For future use
    @GetMapping({"/error", "/api/v1/error"})
    public String errorPage(BindingResult result, Model model) {
        result.reject("errorCode1", "Global Error Happened");
        result.rejectValue("newField", "Error 2 happened");
        model.addAttribute("error", "");
        return "/api/v1/error";
    }

}
