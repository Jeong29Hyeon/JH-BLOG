package com.study.blog.mapper;

import com.study.blog.domain.comment.CommentRequest;
import com.study.blog.domain.comment.CommentResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    void saveReply(CommentRequest commentRequest);

    List<CommentResponse> findComments(Long postId);

    List<CommentResponse> findReplies(Long id);

    @Select("select count(*) from comment where parent_id = #{id} and depth = 2")
    int countRepliesById(Long id);

    @Delete("delete from comment where id=#{id}")
    void delete(Long id);

    @Update("update comment set content = #{content}, modified_date = now() where id = #{id}")
    void update(CommentRequest commentRequest);

    CommentResponse findOne(Long commentId);

    void saveComment(CommentRequest commentRequest);
}
