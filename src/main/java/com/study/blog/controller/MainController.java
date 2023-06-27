package com.study.blog.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "redirect:/post";
    }

    @GetMapping("/oauth2-login")
    public String oauth2Login() {
        return "login";
    }
}
