package com.study.blog.service;

import com.study.blog.domain.comment.CommentRequest;
import com.study.blog.domain.comment.CommentResponse;
import com.study.blog.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;

    public Long save(final CommentRequest commentRequest) {
        commentMapper.save(commentRequest);
        return commentRequest.getId();
    }

    public List<CommentResponse> findComments() {
        return commentMapper.findComments();
    }

    public List<CommentResponse> findReplies(final Long id) {
        return commentMapper.findReplies(id);
    }

    public void delete(final Long id) {
        int count = commentMapper.countRepliesById(id);
        if(count != 0) {
            throw new RuntimeException("답글이 달린 댓글은 삭제할 수 없습니다.");
        }
        commentMapper.delete(id);
    }

    public void update(final CommentRequest commentRequest) {
        commentMapper.update(commentRequest);
    }
}
