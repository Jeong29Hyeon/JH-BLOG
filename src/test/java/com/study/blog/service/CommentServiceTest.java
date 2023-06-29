package com.study.blog.service;

import com.study.blog.domain.comment.CommentRequest;
import com.study.blog.domain.comment.CommentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired private CommentService commentService;

    @Test
    @DisplayName("댓글을 저장한다.")
    public void saveComment() throws Exception {
        //given
        CommentRequest commentRequest = CommentRequest.builder().postId(13L).content("댓글3")
                                        .userId(3L).parentId(0L).depth(1).build();
        //when
        Long savedId = commentService.save(commentRequest);
        List<CommentResponse> comments = commentService.findComments(13L);

        //then
        comments.stream().filter((comment)-> comment.getId().equals(savedId)).findFirst()
                .ifPresent(comment-> System.out.println(comment.getId()));
    }

    @Test
    @DisplayName("대댓글을 저장한다.")
    public void saveReply() throws Exception {
        //given
        CommentRequest commentRequest = CommentRequest.builder().postId(13L).content("댓글3의 1")
                .userId(3L).parentId(6L).depth(2).build();
        //when
        Long savedId = commentService.save(commentRequest);
        List<CommentResponse> comments = commentService.findReplies(2L);
        //then
        for (CommentResponse comment : comments) {
            System.out.println(comment.getContent());
        }
     }

     @Test
     @DisplayName("모든 댓글을 조회하면 댓글의 정보와 회원의 정보도 나와야한다.")
     public void findComments() throws Exception {
         List<CommentResponse> comments = commentService.findComments(13L);
         for (CommentResponse comment : comments) {
             System.out.println("comment.toString() = " + comment.toString());
         }
      }
}