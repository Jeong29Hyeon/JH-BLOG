package com.study.blog.mapper;

import com.study.blog.domain.Users;
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

    @Update("update users set nickname = #{nickname} where id=#{id}")
    int update(Users user);

    @Update("update users set image_url = #{imageUrl},social_name = #{socialName} where id=#{id}")
    int updateLoginUserInfo(Users user);

    int save(Users newUser);

    @Select("select * from users where id=#{userId}")
    Users findById(Long userId);

    @Select("select count(*) from users where nickname = #{nickname};")
    int existNickname(String nickname);
}
