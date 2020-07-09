package com.example.serviceinformation.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.base.service.BaseConstant;
import com.example.common.dto.input.CommentPageInputDTO;
import com.example.common.dto.input.PageInputDTO;
import com.example.common.dto.input.ParamsInputDTO;
import com.example.common.dto.input.StorageInformationInputDTO;
import com.example.common.dto.output.CommentReplyStatusOutputDTO;
import com.example.common.dto.output.CommentStatusOutputDTO;
import com.example.common.dto.output.InformationOutputDTO;
import com.example.common.utils.SysConst;
import com.example.common.utils.SysConst.TopicType;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.dto.DtoUtils;
import com.example.datasource.entity.Comment;
import com.example.datasource.entity.CommentReply;
import com.example.datasource.entity.Information;
import com.example.serviceinformation.service.AdditionalService;
import com.example.serviceinformation.service.InformationService;
import com.example.shiro.aop.DistributedLock;
import com.example.shiro.aop.SaveLog;
import com.example.shiro.base.BaseController;
import com.example.shiro.base.CurrentUser;
import com.example.shiro.base.ResultMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 17:58
 * description:
 */
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/information")
public class InformationController extends BaseController implements CurrentUser, BaseConstant {

    private final InformationService informationService;

    private final AdditionalService additionalService;

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "saveInformation")
    @SaveLog(desc = "保存资讯信息")
    @DistributedLock
    public ResultMessage saveInformation(@Valid @RequestBody StorageInformationInputDTO storageInformation) {
        Information information = DtoUtils.convertToInformation(storageInformation);
        information.setUserId(getCurrentUserId());
        InformationOutputDTO roInformation = informationService.saveInformation(information);
        return success("保存成功", roInformation);
    }


    @PostMapping(value = "saveInformationImg")
    @SaveLog(desc = "保存资讯图片信息")
    @DistributedLock
    public ResultMessage saveInformationImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        informationService.modifyInformationSateToNewRelease(topicId);
        additionalService.saveTopicImgFile(request, topicId, TOPIC_TYPE_2);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "deleteInformation")
    @SaveLog(desc = "删除资讯信息")
    @DistributedLock
    public ResultMessage deleteInformation(@NotNull(message = "id不能为空") @RequestParam Long id) {
        informationService.deleteInformation(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "findInformation")
    public ResultMessage findInformation(@NotNull(message = "id不能为空") @RequestParam Long id) {
        InformationOutputDTO information = informationService.findRoInformation(id, getCurrentUserId());
        return success(information);
    }

    @PostMapping(value = "findInformationEffective")
    public ResultMessage findInformationEffective(@Valid @RequestBody ParamsInputDTO params) {
        Information information = DtoUtils.convertToInformation(params);
        PageImpl<InformationOutputDTO> page = informationService.findInformationEffectivePage(information, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @PostMapping(value = "findInformationUser")
    public ResultMessage findInformationUser(@Valid @RequestBody ParamsInputDTO params) {
        Information information = DtoUtils.convertToInformation(params);
        Long currentUserId = getCurrentUserId();
        information.setUserId(currentUserId);
        PageImpl<InformationOutputDTO> page = informationService.findInformationUserPage(information, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @PostMapping(value = "findInformationCollection")
    public ResultMessage findInformationCollection(@Valid @RequestBody PageInputDTO pageInputDTO) {
        CustomPage customPage = DtoUtils.convertToCustomPage(pageInputDTO);
        PageImpl<InformationOutputDTO> page = informationService.findInformationCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping(value = "enableInformationZanOn")
    @SaveLog(desc = "保存资讯信息点赞")
    @DistributedLock
    public ResultMessage enableInformationZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        additionalService.enableOnZan(id, TOPIC_TYPE_2, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @PostMapping(value = "enableInformationZanOff")
    @SaveLog(desc = "保存资讯信息取消点赞")
    @DistributedLock
    public ResultMessage enableInformationZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        additionalService.enableOffZan(id, TOPIC_TYPE_2, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping(value = "enableInformationCollectionOn")
    @SaveLog(desc = "保存资讯信息收藏")
    @DistributedLock
    public ResultMessage enableInformationCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        additionalService.enableOnCollection(getCurrentUserId(), id, TOPIC_TYPE_2);
        return success("保存成功");
    }

    @PostMapping(value = "enableInformationCollectionOff")
    @SaveLog(desc = "保存资讯信息取消收藏")
    @DistributedLock
    public ResultMessage enableInformationCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        additionalService.enableOffCollection(getCurrentUserId(), id, TOPIC_TYPE_2);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "findInformationComment")
    public ResultMessage findInformationComment(@RequestBody CommentPageInputDTO voCommentPage) {
        Comment comment = DtoUtils.convertToComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_2.getCode());
        PageImpl<CommentStatusOutputDTO> roCommentStatusPage = additionalService.findCommentStatusDtoPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @PostMapping(value = "findInformationCommentCount")
    public ResultMessage findInformationCommentCount(@NotNull(message = "topicId不能为空") @RequestParam Long topicId) {
        JSONObject result = additionalService.countComment(topicId, TopicType.TOPIC_TYPE_2.getCode());
        return success(result);
    }

    @PostMapping(value = "findInformationCommentReply")
    public ResultMessage findInformationCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<CommentReplyStatusOutputDTO> roCommentStatusList = additionalService.findCommentReplyStatusDtoList(commentId);
        return success(roCommentStatusList);
    }

    @PostMapping(value = "findInformationCommentAndReply")
    public ResultMessage findInformationCommentAndReply(@RequestBody CommentPageInputDTO voCommentPage) {
        Comment comment = DtoUtils.convertToComment(voCommentPage);
        comment.setTopicType(TopicType.TOPIC_TYPE_2.getCode());
        PageImpl<CommentStatusOutputDTO> roCommentStatusPage = additionalService.findCommentAndReplyStatusDtoPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "saveInformationComment")
    @SaveLog(desc = "保存资讯信息评论")
    @DistributedLock
    public ResultMessage saveInformationComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = additionalService.saveComment(topicId, TOPIC_TYPE_2, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @PostMapping(value = "saveInformationCommentReplyToComment")
    @SaveLog(desc = "保存资讯信息回复")
    @DistributedLock
    public ResultMessage saveInformationCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                              @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                              @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                              @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = additionalService.saveCommentReplyToComment(topicId, TOPIC_TYPE_2, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @PostMapping(value = "saveInformationCommentReplyToReply")
    @SaveLog(desc = "保存资讯信息回复的回复")
    @DistributedLock
    public ResultMessage saveInformationCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                            @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                            @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                            @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = additionalService.saveCommentReplyToReply(topicId, TOPIC_TYPE_2, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////

    @PostMapping(value = "enableInformationCommentZanOn")
    @SaveLog(desc = "保存资讯信息评论点赞")
    @DistributedLock
    public ResultMessage enableInformationCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                       @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        additionalService.enableOnZan(id, TOPIC_TYPE_2, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @PostMapping(value = "enableInformationCommentZanOff")
    @SaveLog(desc = "保存资讯信息点赞")
    @DistributedLock
    public ResultMessage enableInformationCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        additionalService.enableOffZan(id, TOPIC_TYPE_2, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }


}
