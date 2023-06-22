package com.study.blog.controller;

import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import com.study.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/write")
    public String openWriteForm(@RequestParam(value="id",required = false)final Long id,
                                Model model) {
        if(id != null) {
            PostResponse findPost = postService.findPostById(id);
            model.addAttribute("post",findPost);
        }
        return "post/write";
    }

    @PostMapping("/save")
    public String savePost(final PostRequest postRequest){
        postService.savePost(postRequest);
        return "redirect:/post";
    }

    @GetMapping("/view")
    public String viewPost(@RequestParam("id") final Long id, Model model) {
        PostResponse findPost = postService.findPostById(id);
        model.addAttribute("post",findPost);
        return "post/view";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("id")final Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }
}
