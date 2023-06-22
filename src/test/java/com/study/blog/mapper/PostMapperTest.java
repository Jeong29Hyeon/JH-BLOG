package com.study.blog.mapper;

import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostMapperTest {
    private final PostMapper postMapper;

    @Autowired
    public PostMapperTest(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Test
    @DisplayName("포스트를 저장한다.")
    public void savePost() throws Exception {
        //given
        PostRequest postRequest = PostRequest.builder()
                .title("제목1")
                .content("내용1")
                .build();
        //when
        postMapper.save(postRequest);
        PostResponse findPost = postMapper.findById(postRequest.getId());

        //then
        assertThat(findPost.getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(findPost.getId()).isEqualTo(postRequest.getId());
     }

    @Test
    @DisplayName("PK 1번 포스트를 수정한다.")
    public void updatePost() throws Exception {
        //given
        PostRequest post = PostRequest.builder()
                .id(1L)
                .title("제목1수정")
                .content("내용1수정")
                .build();
        //when
        postMapper.update(post);
        PostResponse findPost = postMapper.findById(post.getId());
        //then
        assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(findPost.getContent()).isEqualTo(post.getContent());
     }
}