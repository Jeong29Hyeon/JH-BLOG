package com.study.blog.restapi;

import com.study.blog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostImageController {
    private final ImageService imageService;

    @PostMapping("/post-image/upload")
    public ResponseEntity<Map<String,String>> uploadPostImage(
            @RequestParam("postImage")final MultipartFile file) {
        Map<String,String> response = new HashMap<>();
        String saveFileName = null;
        try {
            saveFileName = imageService.uploadPostImage(file);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msg",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        if(saveFileName == null) {
            response.put("msg","업로드파일이 존재하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("saveFileName",saveFileName);
        String originalFileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        response.put("originalFileName",originalFileName);
        return ResponseEntity.ok().body(response);
    }
}
