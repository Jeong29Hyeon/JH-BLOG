package com.study.blog.controller;

import com.study.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping()
    public String openBlog(Model model) {
        model.addAttribute("posts",postService.findAllPost());
        return "post/main";
    }
    
}
