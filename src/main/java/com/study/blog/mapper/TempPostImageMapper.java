package com.study.blog.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TempPostImageMapper {
    @Insert("insert into temp_post_image(image_name) values (#{imageName})")
    void insertImageName(String imageName);

    @Select("select image_name from temp_post_image;")
    List<String> findAllImageName();

    @Delete("delete from temp_post_image;")
    void deleteAll();
}
