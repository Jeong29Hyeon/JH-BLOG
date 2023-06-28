package com.study.blog.mapper;

import com.study.blog.domain.comment.CommentRequest;
import com.study.blog.domain.comment.CommentResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface CommentMapper {
    void save(CommentRequest commentRequest);

    List<CommentResponse> findComments();

    List<CommentResponse> findReplies(Long id);

    @Select("select count(*) from comment where parent_id = #{id} and depth = 2")
    int countRepliesById(Long id);

    @Delete("delete from comment where id=#{id}")
    void delete(Long id);

    @Update("update comment set content = #{content} where id = #{id}")
    void update(CommentRequest commentRequest);
}
