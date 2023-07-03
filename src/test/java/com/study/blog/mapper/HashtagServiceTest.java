package com.study.blog.mapper;

import com.study.blog.domain.hashtag.Hashtag;
import com.study.blog.domain.post.PostRequest;
import com.study.blog.domain.post.PostResponse;
import com.study.blog.dto.SearchDto;
import com.study.blog.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HashtagServiceTest {
    @Autowired HashtagMapper hashtagMapper;
    @Autowired PostTagMapper postTagMapper;
    @Autowired PostMapper postMapper;
    @Autowired
    PostService postService;

    @Test
    @DisplayName("해시태그들을 저장한다.")
    public void save() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("test3")
                .description("test3")
                .content("test3")
                .build();
        postRequest.setThumbnail("default.gif");
        List<String> tagNames = new ArrayList<>();
        tagNames.add("mybatis");
        tagNames.add("jpa");
        tagNames.add("일상");
        Long postId = postService.savePost(postRequest, tagNames);
     }

     @Test
     @DisplayName("특정 해시태그 이름으로 관련된 게시물을 찾는다.")
     public void findPostByHashtag() throws Exception {
         //given
         String hashtagName = "mybatis";
         Hashtag springTag = hashtagMapper.findByName(hashtagName);
         SearchDto searchDto = new SearchDto();
         searchDto.setHashtag(springTag.getName());
         //when
         List<PostResponse> springList = postMapper.findAll(searchDto);
         //then
         for (PostResponse postResponse : springList) {
             System.out.println("postResponse.getContent() = " + postResponse.getContent());
         }
      }

      @Test
      @DisplayName("해시태그를 수정한다.")
      public void updateHashTag() throws Exception {
          //given
          PostResponse findPost = postMapper.findById(7L);
          PostRequest postRequest = PostRequest.builder()
                  .id(findPost.getId())
                  .title(findPost.getTitle())
                  .content(findPost.getContent())
                  .description(findPost.getDescription())
                  .build();
          postRequest.setThumbnail("default.gif");
          List<String> modifyTagNames = new ArrayList<>();
          modifyTagNames.add("spring");
          modifyTagNames.add("mybatis");
          modifyTagNames.add("jpa");
          //when 중복저장은 허용하지 않기때문에 안들어가고 jpa만 추가로 들어감
            postService.updatePost(postRequest,modifyTagNames);
          //then
       }

       @Test
       @DisplayName("포스트를 삭제하면 사용되지 않는 해시태그도 삭제된다.")
       public void deleteHashtag() throws Exception {
           postService.deletePost(9L);
        }
}