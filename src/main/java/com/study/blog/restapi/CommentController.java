package com.study.blog.restapi;

import com.study.blog.domain.Role;
import com.study.blog.domain.comment.CommentRequest;
import com.study.blog.domain.comment.CommentResponse;
import com.study.blog.dto.SessionUser;
import com.study.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments") //댓글 조회
    public List<CommentResponse> findAllComments(
            @PathVariable final Long postId){
        return commentService.findComments(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public CommentResponse findOneComment( //댓글 하나 조회
            @PathVariable final Long postId,
            @PathVariable final Long commentId) {
        return commentService.findOne(commentId);
    }


    @PostMapping("/posts/{postId}/comments") //신규 댓글 저장
    public Map<String,Object> saveComment(
            @PathVariable final Long postId,
            @RequestBody final CommentRequest commentRequest) {
        Map<String,Object> response = new HashMap<>();
        if(commentRequest.getParentId() == 0L) {
            commentService.saveComment(commentRequest);
        } else {
            CommentResponse findComment = commentService.findOne(commentRequest.getParentId());
            if(findComment == null || findComment.getDepth() == 2) {
                response.put("msg","해당 댓글이 존재하지 않습니다.");
                return response;
            }
            commentService.saveReply(commentRequest);
        }
        response.put("msg","댓글이 저장되었습니다.");
        return response;
    }

    @PatchMapping("/posts/{postId}/comments/{id}")
    public Map<String,Object> updateComment( //댓글 수정
            @PathVariable final Long postId,
            @PathVariable final Long id,
            @RequestBody final CommentRequest commentRequest,
                                             HttpSession session) {
        Map<String,Object> response = new HashMap<>();
        SessionUser user = (SessionUser) session.getAttribute("user");
        CommentResponse findComment = commentService.findOne(id);
        if(findComment.getUser().getId().equals(user.getId()) || user.getRole() == Role.USER) {
            commentService.update(commentRequest);
            response.put("msg","댓글이 수정되었습니다.");
            return response;
        }
        response.put("msg","자기가 쓴 댓글만 수정할 수 있습니다.");
        return response;
    }

    //댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public Map<String,Object> deleteComment(@PathVariable final Long postId,
                                            @PathVariable final Long id,
                                            HttpSession session) {
        Map<String,Object> response = new HashMap<>();
        SessionUser user = (SessionUser) session.getAttribute("user");
        CommentResponse findComment = commentService.findOne(id);
        if(findComment.getUser().getId().equals(user.getId()) || user.getRole() == Role.USER) {
            try {
                commentService.delete(id);
            } catch (Exception e) {
                response.put("msg",e.getMessage());
                return response;
            }
            response.put("msg","댓글이 삭제되었습니다.");
            return response;
        }
        response.put("msg","자기가 쓴 댓글만 삭제할 수 있습니다.");
        return response;
    }



}
