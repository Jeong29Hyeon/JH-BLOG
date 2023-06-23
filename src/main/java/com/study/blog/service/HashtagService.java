package com.study.blog.service;

import com.study.blog.domain.Hashtag;
import com.study.blog.domain.PostTag;
import com.study.blog.mapper.HashtagMapper;
import com.study.blog.mapper.PostTagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagMapper hashtagMapper;
    private final PostTagMapper postTagMapper;


    public void saveHashtag(List<String>tagNames, Long postId) {
        if(tagNames == null || tagNames.isEmpty()){
            return;
        }
        List<Long> tagIdList = new ArrayList<>();
        Hashtag newTag = null;
        for (String tagName : tagNames) {
            Hashtag findHashtag = hashtagMapper.findByName(tagName);
            if(findHashtag == null) {
                newTag = Hashtag.builder().name(tagName).build();
                hashtagMapper.save(newTag);
                tagIdList.add(newTag.getId());
            } else {
                tagIdList.add(findHashtag.getId());
            }
        }
        savePostTag(tagIdList,postId);
    }

    private void savePostTag(List<Long> tagIdList, Long postId) {
        for (Long tagId : tagIdList) {
            PostTag postTag = PostTag.builder()
                    .postId(postId)
                    .tagId(tagId)
                    .build();
            postTagMapper.save(postTag);
        }
    }


    public void updateHashtag(List<String> tagNames, Long postId) {
        if(tagNames == null || tagNames.isEmpty()) {
            tagNames = new ArrayList<>();
        }
        List<String> findTagNames = postTagMapper.findNameByPostId(postId);
        Map<String,Object> map = new HashMap<>();
        map.put("postId",postId);
        for (String findTagName : findTagNames) { //기존 해시태그가
            if (!tagNames.contains(findTagName)) { //업데이트 된 태그 리스트에 존재하지 않으면
                map.put("tagName",findTagName);
                postTagMapper.deleteByPostIdAndTagName(map); // 삭제
                deleteNotUsedHashtag(findTagName);
            } else { //존재하면 중복 저장을 방지하기 위해 리스트에서 제외
                tagNames.remove(findTagName);
            }
        }
        saveHashtag(tagNames,postId);
    }

    //태그이름으로 조회해서 0이면 사용하지않으니 삭제
    private void deleteNotUsedHashtag(String tagName) {
        int count = postTagMapper.countByTagName(tagName);
        if(count == 0) {
            hashtagMapper.deleteByName(tagName);
        }
    }

    public void deleteByPostId(Long postId) {
        List<String> tagNames = postTagMapper.findNameByPostId(postId);
        postTagMapper.deleteByPostId(postId);
        for (String tagName : tagNames) {
            deleteNotUsedHashtag(tagName);
        }
    }
}
