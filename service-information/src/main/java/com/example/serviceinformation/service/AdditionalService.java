package com.example.serviceinformation.service;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.output.*;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.entity.*;
import com.example.serviceinformation.service.fallback.AdditionalServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 13:41
 * description:
 */
@FeignClient(name = "SERVICE-ADDITIONAL", fallbackFactory = AdditionalServiceFallbackFactory.class, path = "service/additional")
public interface AdditionalService {

    /**
     * resultInformationDto
     *
     * @param information information
     * @param userId      userId
     * @return InformationOutputDTO
     */
    @PostMapping("resultInformationDto")
    InformationOutputDTO resultInformationDto(@RequestBody Information information, @RequestParam Long userId);

    /**
     * resultInformationDtoPage
     *
     * @param list   list
     * @param userId userId
     * @return PageImpl
     */
    @PostMapping("resultInformationDtoList")
    List<InformationOutputDTO> resultInformationDtoList(@RequestBody List<Information> list, @RequestParam Long userId);

    /**
     * saveTopicImgFile
     *
     * @param request   request
     * @param topicId   topicId
     * @param topicType topicType
     */
    @PostMapping("saveTopicImgFile")
    void saveTopicImgFile(HttpServletRequest request, @RequestParam Long topicId, @RequestParam Short topicType);

    /**
     * findCommentStatusDtoPage
     *
     * @param comment comment
     * @param userId  userId
     * @return PageImpl
     */
    @PostMapping("findCommentStatusDtoPage")
    PageImpl<CommentStatusOutputDTO> findCommentStatusDtoPage(@RequestBody Comment comment, @RequestParam Long userId);

    /**
     * countComment
     *
     * @param topicId   topicId
     * @param topicType topicType
     * @return JSONObject
     */
    @PostMapping("countComment")
    JSONObject countComment(@RequestParam Long topicId, @RequestParam Short topicType);

    /**
     * findCommentAndReplyStatusDtoPage
     *
     * @param comment comment
     * @param userId  userId
     * @return PageImpl
     */
    @PostMapping("findCommentAndReplyStatusDtoPage")
    PageImpl<CommentStatusOutputDTO> findCommentAndReplyStatusDtoPage(@RequestBody Comment comment, @RequestParam Long userId);

    /**
     * saveComment
     *
     * @param topicId    topicId
     * @param topicType  topicType
     * @param content    content
     * @param toUserId   toUserId
     * @param fromUserId * @return
     * @return Comment
     */
    @PostMapping("saveComment")
    Comment saveComment(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId);

    /**
     * findCommentReplyStatusDtoList
     *
     * @param commentId commentId
     * @return List
     */
    @PostMapping("findCommentReplyStatusDtoList")
    List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(@RequestParam Long commentId);

    /**
     * saveCommentReplyToComment
     *
     * @param topicId    topicId
     * @param topicType  topicType
     * @param commentId  commentId
     * @param replyId    replyId
     * @param content    content
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @return CommentReply
     */
    @PostMapping("saveCommentReplyToComment")
    CommentReply saveCommentReplyToComment(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Long commentId, @RequestParam Long replyId, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId);

    /**
     * saveCommentReplyToReply
     *
     * @param topicId    topicId
     * @param topicType  topicType
     * @param commentId  commentId
     * @param replyId    replyId
     * @param content    content
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @return CommentReply
     */
    @PostMapping("saveCommentReplyToReply")
    CommentReply saveCommentReplyToReply(@RequestParam Long topicId, Short topicType, @RequestParam Long commentId, @RequestParam Long replyId, @RequestParam String content, @RequestParam Long toUserId, @RequestParam Long fromUserId);

    /**
     * enableOnZan
     *
     * @param topicId    topicId
     * @param topicType  topicType
     * @param zanType    zanType
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     */
    @PostMapping("enableOnZan")
    void enableOnZan(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Short zanType, @RequestParam Long toUserId, @RequestParam Long fromUserId);

    /**
     * enableOffZan
     *
     * @param topicId    topicId
     * @param topicType  topicType
     * @param zanType    zanType
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     */
    @PostMapping("enableOffZan")
    void enableOffZan(@RequestParam Long topicId, @RequestParam Short topicType, @RequestParam Short zanType, @RequestParam Long toUserId, @RequestParam Long fromUserId);

    /**
     * enableOnCollection
     *
     * @param userId    userId
     * @param topicId   topicId
     * @param topicType topicType
     */
    @PostMapping("enableOnCollection")
    void enableOnCollection(@RequestParam Long userId, @RequestParam Long topicId, @RequestParam Short topicType);

    /**
     * enableOffCollection
     *
     * @param userId    userId
     * @param topicId   topicId
     * @param topicType topicType
     */
    @PostMapping("enableOffCollection")
    void enableOffCollection(@RequestParam Long userId, @RequestParam Long topicId, @RequestParam Short topicType);

    /**
     * findCollection
     *
     * @param userId     userId
     * @param topicType  topicType
     * @param customPage customPage
     * @return PageImpl
     */
    @PostMapping("findCollection")
    PageImpl<Long> findCollection(@RequestParam Long userId, @RequestParam Short topicType, @RequestBody CustomPage customPage);
}
