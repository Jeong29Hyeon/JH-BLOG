package com.study.blog.mapper;

import com.study.blog.domain.Hashtag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HashtagMapper {
    int save(Hashtag hashtag);

    Hashtag findByName(String name);

    void deleteByName(String name);

    @Select(value = "select * from hashtag")
    List<Hashtag> findAll();
}
