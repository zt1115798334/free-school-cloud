package com.example.serviceadditional.service;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.output.CommentStatusOutputDTO;
import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.Comment;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentService extends BaseService<Comment, Long> {

    Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId);

    void adoptComment(Long id, Long topicId, Short topicType);

    Comment findComment(Long id);

    JSONObject countComment(Long topicId, Short topicType);

    Map<Long, Long> countComment(List<Long> topicId, Short topicType);

    PageImpl<CommentStatusOutputDTO> findCommentStatusDtoPage(Comment comment, Long userId);

    PageImpl<CommentStatusOutputDTO> findCommentAndReplyStatusDtoPage(Comment comment, Long userId);

}
