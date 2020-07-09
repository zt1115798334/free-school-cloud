package com.example.serviceadditional.controller.service;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.output.*;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.entity.*;
import com.example.serviceadditional.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 13:12
 * description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("service/additional")
public class AdditionalController {

    private final TopicService topicService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;


    @PostMapping("resultInformationDto")
    public InformationOutputDTO resultInformationDto(@RequestBody Information information, @RequestParam Long userId) {
        return topicService.resultInformationDto(information, userId);
    }

    @PostMapping("resultInformationDtoList")
    public List<InformationOutputDTO> resultInformationDtoList(@RequestBody List<Information> list, @RequestParam Long userId) {
        return topicService.resultInformationDtoList(list, userId);
    }


    @PostMapping("saveTopicImgFile")
    public void saveTopicImgFile(HttpServletRequest request, @RequestParam Long topicId, @RequestParam Short topicType) {
        topicImgService.saveTopicImgFile(request, topicId, topicType);
    }

    @PostMapping("findCommentStatusDtoPage")
    public PageImpl<CommentStatusOutputDTO> findCommentStatusDtoPage(@RequestBody Comment comment, @RequestParam Long userId) {
        return commentService.findCommentStatusDtoPage(comment, userId);
    }

    @PostMapping("countComment")
    public JSONObject countComment(@RequestParam Long topicId, @RequestParam Short topicType) {
        return commentService.countComment(topicId, topicType);
    }

    @PostMapping("findCommentAndReplyStatusDtoPage")
    public PageImpl<CommentStatusOutputDTO> findCommentAndReplyStatusDtoPage(@RequestBody Comment comment, @RequestParam Long userId) {
        return commentService.findCommentAndReplyStatusDtoPage(comment, userId);
    }

    @PostMapping("saveComment")
    public Comment saveComment(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId) {
        return commentService.saveComment(topicId, topicType, content, toUserId, fromUserId);
    }

    @PostMapping("findCommentReplyStatusDtoList")
    public List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(@RequestParam Long commentId) {
        return commentReplyService.findCommentReplyStatusDtoList(commentId);
    }

    @PostMapping("saveCommentReplyToComment")
    public CommentReply saveCommentReplyToComment(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Long commentId, @RequestParam Long replyId, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId) {
        return commentReplyService.saveCommentReplyToComment(topicId, topicType, commentId, replyId, content, toUserId, fromUserId);
    }

    @PostMapping("saveCommentReplyToReply")
    public CommentReply saveCommentReplyToReply(@RequestParam Long topicId, Short topicType, @RequestParam Long commentId, @RequestParam Long replyId, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId) {
        return commentReplyService.saveCommentReplyToReply(topicId, topicType, commentId, replyId, content, toUserId, fromUserId);
    }

    @PostMapping("enableOnZan")
    public void enableOnZan(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Short zanType, @RequestParam Long toUserId, @RequestParam Long fromUserId) {
        zanService.enableOnZan(topicId, topicType, zanType, toUserId, fromUserId);
    }

    @PostMapping("enableOffZan")
    public void enableOffZan(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Short zanType, @RequestParam Long toUserId, @RequestParam Long fromUserId) {
        zanService.enableOffZan(topicId, topicType, zanType, toUserId, fromUserId);
    }

    @PostMapping("enableOnCollection")
    public void enableOnCollection(@RequestParam Long userId, @RequestParam Long topicId, @RequestParam Short topicType) {
        collectionService.enableOnCollection(userId, topicId, topicType);
    }

    @PostMapping("enableOffCollection")
    public void enableOffCollection(@RequestParam Long userId, @RequestParam Long topicId, @RequestParam Short topicType) {
        collectionService.enableOffCollection(userId, topicId, topicType);
    }

    @PostMapping("findCollection")
    public PageImpl<Long>  findCollection(@RequestParam Long userId, @RequestParam Short topicType,@RequestBody CustomPage customPage) {
        return collectionService.findCollection(userId, topicType, customPage);
    }
}
