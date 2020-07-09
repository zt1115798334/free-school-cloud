package com.example.serviceadditional.service;

import com.example.common.dto.output.CommentReplyStatusOutputDTO;
import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.CommentReply;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentReplyService extends BaseService<CommentReply, Long> {

    CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    List<CommentReply> findCommentReplyList(Long commentId);

    List<CommentReply> findCommentReplyList(List<Long> commentIdList);

    List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(Long commentId);

    List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(List<Long> commentIdList);

}
