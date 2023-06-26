package com.study.blog.mapper;

import com.study.blog.domain.Users;
import com.study.blog.oauth2.SocialType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select * from users where email = #{email}")
    Users findByEmail(String email);

    @Select("select * from users where social_type = #{socialType} and social_id = #{socialId}")
    Users findBySocialTypeAndSocialId(Map<String,Object> map);

    @Update("update users set name = #{name} where id=#{id}")
    void update(Users user);

    int save(Users newUser);
}
