package com.study.blog.service;

import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import com.study.blog.dto.Pagination;
import com.study.blog.dto.SearchDto;
import com.study.blog.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final HashtagService hashtagService;
    /**
     * 포스트 저장
     * @param postRequest - 포스트 정보
     * @return - keyGenerated 에 의해 생성된 기본키
     */
    @Transactional
    public Long savePost(final PostRequest postRequest, final List<String> tagNames) {
        postMapper.save(postRequest);
        hashtagService.saveHashtag(tagNames,postRequest.getId());
        return postRequest.getId();
    }

    /**
     * 포스트 상세정보 조회
     * @param id - PK
     * @return 포스트 상세정보
     */
    public PostResponse findPostById(final Long id) {
        return postMapper.findById(id);
    }

    /**
     * 포스트 수정
     * @param postRequest - 포스트 정보
     * @return PK
     */
    @Transactional
    public Long updatePost(final PostRequest postRequest, final List<String>tagNames) {
        postMapper.update(postRequest);
        hashtagService.updateHashtag(tagNames, postRequest.getId());
        return postRequest.getId();
    }

    /**
     * 게시글 삭제
     * @param id - PK
     * @return PK
     */
    @Transactional
    public Long deletePost(final Long id) {
        postMapper.deleteById(id);
        hashtagService.deleteByPostId(id);
        return id;
    }

    /**
     * 게시글 리스트 조회
     * @return 게시글 리스트
     */
    public List<PostResponse> findAllPost(final SearchDto searchDto) {
        int count = postMapper.count(searchDto);
        if(count < 1) {
            return Collections.emptyList();
        }
        System.out.println(count);
        Pagination pagination = new Pagination(count,searchDto);
        return postMapper.findAll(searchDto);
    }
}
