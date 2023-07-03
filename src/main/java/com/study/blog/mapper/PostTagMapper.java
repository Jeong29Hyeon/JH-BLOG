package com.study.blog.mapper;

import com.study.blog.domain.hashtag.PostTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostTagMapper {
    void save(PostTag postTag);

    void deleteByPostId(Long postId);

    List<String> findNameByPostId(Long postId);

    void deleteByPostIdAndTagName(Map<String,Object> map);

    int countByTagName(String tagName);

}
