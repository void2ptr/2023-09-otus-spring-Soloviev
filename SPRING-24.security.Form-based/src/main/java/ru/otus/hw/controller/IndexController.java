package ru.otus.hw.controller;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping({"/", "/index", "/index.html"})
    public String indexPage(Model model) {
        return "/index";
    }

    @GetMapping({"/error"})
    public String errorPage(BindingResult result, Model model) {
        result.reject("errorCode1", "Global Error Happened");
        result.rejectValue("newField", "Error 2 happened");
        model.addAttribute("error", "");
        return "/error";
    }

}
