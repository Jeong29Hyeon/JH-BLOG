package com.study.blog.controller;

import com.study.blog.domain.Users;
import com.study.blog.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");
        if(sessionUser != null) {
            model.addAttribute("user",sessionUser);
        }
        return "index";
    }
}
