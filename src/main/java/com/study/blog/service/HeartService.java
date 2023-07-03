package com.study.blog.service;

import com.study.blog.domain.Heart;
import com.study.blog.mapper.HeartMapper;
import com.study.blog.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartMapper heartMapper;
    private final PostMapper postMapper;

    @Transactional
    public Map<String,Object> likeHit(Heart heart) {
        Map<String,Object> response = new HashMap<>();
        Heart findHeart = heartMapper.findOne(heart);
        if(findHeart != null) {
            heartMapper.delete(heart);
            postMapper.decreaseLike(heart.getPostId());
            response.put("msg","좋아요를 취소했습니다.");
            response.put("result", -1);
            return response;
        }
        heartMapper.insert(heart);
        postMapper.increaseLike(heart.getPostId());
        response.put("msg","좋아요를 눌렀습니다.");
        response.put("result", 1);
        return response;
    }

    public Map<String,Object> likeCheck(Heart heart) {
        Map<String,Object> map = new HashMap<>();
        Heart findHeart = heartMapper.findOne(heart);
        if(findHeart != null) {
            map.put("result",1);
        }else{
            map.put("result",-1);
        }
        return map;
    }
}
