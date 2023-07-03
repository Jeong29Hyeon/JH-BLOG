package com.study.blog.mapper;

import com.study.blog.domain.hashtag.Hashtag;
import com.study.blog.domain.hashtag.HashtagResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HashtagMapper {
    int save(Hashtag hashtag);

    Hashtag findByName(String name);

    void deleteByName(String name);

    List<HashtagResponse> findAll();

}
