package com.study.blog.restapi;

import com.study.blog.domain.Heart;
import com.study.blog.mapper.HeartMapper;
import com.study.blog.mapper.PostMapper;
import com.study.blog.service.HeartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/like-hit")
    public ResponseEntity<Map<String,Object>> likeHit(@RequestBody Heart heart) {
        Map<String,Object> response = new HashMap<>();
        if(heart.getPostId() == null || heart.getUserId() == null) {
            response.put("msg","올바른 접근이 아닙니다");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            response = heartService.likeHit(heart);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/like-check")
    public ResponseEntity<Map<String,Object>> likeCheck(Heart heart) {
        log.info("heart like check " + heart.getPostId() + heart.getUserId());
        Map<String, Object> response = heartService.likeCheck(heart);
        return ResponseEntity.ok(response);
    }

}
