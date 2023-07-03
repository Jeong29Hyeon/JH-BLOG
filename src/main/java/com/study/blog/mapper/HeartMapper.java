package com.study.blog.mapper;

import com.study.blog.domain.Heart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface HeartMapper {
    @Insert("insert into post_like_users(user_id, post_id, created_date) values (#{userId},#{postId},now())")
    int insert(Heart heart);

    @Delete("delete from post_like_users where post_id = #{postId} and user_id = #{userId}")
    int delete(Heart heart);

    @Select("select user_id, post_id from post_like_users where post_id = #{postId} and user_id = #{userId}")
    Heart findOne(Heart heart);
}
