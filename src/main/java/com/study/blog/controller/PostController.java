package com.study.blog.controller;

import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import com.study.blog.service.ImageService;
import com.study.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final ImageService imageService;

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

    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Map<String, String>> savePost(@RequestParam(value = "image")MultipartFile file,
                                                        final PostRequest postRequest){
        Map<String,String> response = new HashMap<>();
        String saveFileName = null;
        try {
            saveFileName = imageService.uploadImage(file);
        } catch (Exception e) {
            response.put("errorMsg",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        // 이미지 파일이 없으면 dafault.jpg 로 썸네일 잡음
        postRequest.setThumbnail(Objects.requireNonNullElse(saveFileName, "default.jpg"));
        Long id = postService.savePost(postRequest);
        response.put("msg","포스트가 저장되었습니다.");
        response.put("id", String.valueOf(id));
        return ResponseEntity.ok(response);
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

    @PostMapping("/post/update")
    public ResponseEntity<Map<String,String>> updatePost(@RequestParam(value = "image")MultipartFile file,
                                        final PostRequest postRequest) {
        Map<String,String> response = new HashMap<>();
        String saveFileName = null;
        try {
            saveFileName = imageService.uploadImage(file);
        } catch (Exception e) {
            response.put("errorMsg",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        // 이미지 파일이 없으면 dafault.jpg 로 썸네일 잡음
        postRequest.setThumbnail(Objects.requireNonNullElse(saveFileName, "default.jpg"));
        postService.updatePost(postRequest);
        response.put("msg","포스트가 수정되었습니다.");
        response.put("id", String.valueOf(postRequest.getId()));
        return ResponseEntity.ok(response);
    }
}
