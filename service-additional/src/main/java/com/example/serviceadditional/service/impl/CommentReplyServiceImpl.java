package com.example.serviceadditional.service.impl;

import com.example.common.dto.output.CommentReplyStatusOutputDTO;
import com.example.common.dto.output.UserOutputDTO;
import com.example.common.utils.DateUtils;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.CommentReply;
import com.example.datasource.repo.CommentReplyRepository;
import com.example.datasource.utils.UserUtils;
import com.example.serviceadditional.service.CommentReplyService;
import com.example.serviceadditional.service.UserService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
@AllArgsConstructor
@Service
public class CommentReplyServiceImpl implements CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;

    private final UserService userService;

//    private final JPushTool jPushTool;

    @Override
    public CommentReply save(CommentReply commentReply) {
        commentReply.setCreatedTime(currentDateTime());
        commentReply.setDeleteState(UN_DELETED);
        return commentReplyRepository.save(commentReply);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new CommentReply(commentId, SysConst.ReplyType.COMMENT.getCode(), replyId, content, toUserId, fromUserId));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new CommentReply(commentId, SysConst.ReplyType.REPLY.getCode(), replyId, content, toUserId, fromUserId));
    }


    @Override
    public List<CommentReply> findCommentReplyList(Long commentId) {
        return commentReplyRepository.findByCommentIdAndDeleteState(commentId, UN_DELETED);
    }

    @Override
    public List<CommentReply> findCommentReplyList(List<Long> commentIdList) {
        return commentReplyRepository.findByCommentIdInAndDeleteState(commentIdList, UN_DELETED);
    }

    @Override
    public List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(Long commentId) {
        List<CommentReply> commentReplyList = this.findCommentReplyList(commentId);
        return getRoCommentReplyStatuses(commentReplyList);
    }

    @Override
    public List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(List<Long> commentIdList) {
        List<CommentReply> commentReplyList = this.findCommentReplyList(commentIdList);
        return getRoCommentReplyStatuses(commentReplyList);
    }

    private List<CommentReplyStatusOutputDTO> getRoCommentReplyStatuses(List<CommentReply> commentReplyList) {
        List<Long> userIdList = commentReplyList.parallelStream()
                .map(commentReply -> Lists.newArrayList(commentReply.getToUserId(), commentReply.getFromUserId()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, UserOutputDTO> roUserMap = userService.findMapUserDtoByUserId(userIdList);
        return commentReplyList.stream().map(commentReply -> {
            Long toUserId = commentReply.getToUserId();
            Long fromUserId = commentReply.getFromUserId();
            return new CommentReplyStatusOutputDTO(
                    commentReply.getCommentId(), commentReply.getReplyType(), commentReply.getReplyId(),
                    commentReply.getContent(),
                    toUserId, roUserMap.getOrDefault(toUserId, UserUtils.getDefaultUserDto()),
                    fromUserId, roUserMap.getOrDefault(fromUserId, UserUtils.getDefaultUserDto()),
                    DateUtils.formatDateTime(commentReply.getCreatedTime()));
        }).collect(Collectors.toList());
    }

}
