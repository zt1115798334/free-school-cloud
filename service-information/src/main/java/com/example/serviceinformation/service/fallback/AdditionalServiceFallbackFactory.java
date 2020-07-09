package com.example.serviceinformation.service.fallback;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.output.*;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.entity.*;
import com.example.serviceinformation.service.AdditionalService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 13:42
 * description:
 */
@Slf4j
@Component
public class AdditionalServiceFallbackFactory implements FallbackFactory<AdditionalService> {

    @Override
    public AdditionalService create(Throwable throwable) {
        log.error("fallback reason:{}", throwable.getMessage());
        return new AdditionalService() {

            @Override
            public InformationOutputDTO resultInformationDto(Information information, Long userId) {
                return null;
            }

            @Override
            public List<InformationOutputDTO> resultInformationDtoList(List<Information> list, Long userId) {
                return null;
            }

            @Override
            public void saveTopicImgFile(HttpServletRequest request, Long topicId, Short topicType) {

            }

            @Override
            public PageImpl<CommentStatusOutputDTO> findCommentStatusDtoPage(Comment comment, Long userId) {
                return null;
            }

            @Override
            public JSONObject countComment(Long topicId, Short topicType) {
                return null;
            }

            @Override
            public PageImpl<CommentStatusOutputDTO> findCommentAndReplyStatusDtoPage(Comment comment, Long userId) {
                return null;
            }

            @Override
            public Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId) {
                return null;
            }

            @Override
            public List<CommentReplyStatusOutputDTO> findCommentReplyStatusDtoList(Long commentId) {
                return null;
            }

            @Override
            public CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
                return null;
            }

            @Override
            public CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId) {
                return null;
            }

            @Override
            public void enableOnZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId) {

            }

            @Override
            public void enableOffZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId) {

            }

            @Override
            public void enableOnCollection(Long userId, Long topicId, Short topicType) {

            }

            @Override
            public void enableOffCollection(Long userId, Long topicId, Short topicType) {

            }

            @Override
            public PageImpl<Long> findCollection(Long userId, Short topicType, CustomPage customPage) {
                return null;
            }
        };
    }
}
