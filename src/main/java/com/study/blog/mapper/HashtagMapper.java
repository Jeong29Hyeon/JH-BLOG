package com.study.blog.mapper;

import com.study.blog.domain.Hashtag;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface HashtagMapper {
    int save(Hashtag hashtag);

    Hashtag findByName(String name);

    void deleteByName(String name);
}
