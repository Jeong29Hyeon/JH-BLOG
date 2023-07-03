package com.study.blog.mapper;
import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import com.study.blog.dto.SearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper {
    /**
     * 포스트 저장
     * @param postRequest - 포스트 정보
     */
    void save(PostRequest postRequest);

    /**
     * 포스트 상세정보 조회
     * @param id - PK
     * @return 포스트 상세정보
     */
    PostResponse findById(Long id);

    /**
     * 포스트 수정
     * @param postRequest - 포스트 정보
     */
    void update(PostRequest postRequest);

    /**
     * 포스트 삭제
     * @param id - PK
     */
    void deleteById(Long id);

    /**
     * 해시태그에 관련된 게시글 리스트 조회
     * @param searchDto - 해시태그 (나중에 추가될 검색파라미터들)
     * @return - 게시글 리스트
     */
    List<PostResponse> findAll(SearchDto searchDto);

    int count(SearchDto searchDto);

    @Update("update post set like_cnt = like_cnt - 1 where id = #{postId}")
    void decreaseLike(Long postId);

    @Update("update post set like_cnt = like_cnt + 1 where id = #{postId}")
    void increaseLike(Long postId);
}
