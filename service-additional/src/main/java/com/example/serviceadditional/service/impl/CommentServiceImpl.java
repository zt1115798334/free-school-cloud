package com.example.serviceadditional.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.output.CommentReplyStatusOutputDTO;
import com.example.common.dto.output.CommentStatusOutputDTO;
import com.example.common.dto.output.TopicCommentMapOutputDTO;
import com.example.common.dto.output.UserOutputDTO;
import com.example.common.exception.custom.OperationException;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.Comment;
import com.example.datasource.jpa.PageUtils;
import com.example.datasource.jpa.SearchFilter;
import com.example.datasource.repo.CommentRepository;
import com.example.serviceadditional.service.CommentReplyService;
import com.example.serviceadditional.service.CommentService;
import com.example.serviceadditional.service.UserService;
import com.example.serviceadditional.service.ZanService;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentReplyService commentReplyService;

    private final UserService userService;

    private final ZanService zanService;

//    private final JPushTool jPushTool;

    @Override
    public Comment save(Comment comment) {
        comment.setCreatedTime(currentDateTime());
        comment.setDeleteState(UN_DELETED);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long aLong) {
        commentRepository.findById(aLong).ifPresent(comment -> {
            comment.setDeleteState(DELETED);
            commentRepository.save(comment);
        });
    }

    @Override
    public Optional<Comment> findByIdNotDelete(Long aLong) {
        return commentRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public Page<Comment> findPageByEntity(Comment comment) {
        List<SearchFilter> filters = getCommentFilter(comment);
        Specification<Comment> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(comment);
        return commentRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Comment saveComment(Long topicId, Short topicType, String content, Long toUserId, Long fromUserId) {
//        jPushTool.pushCommentInfo(topicId, topicType, content, toUserId, fromUserId);
        return this.save(new Comment(toUserId, topicId, topicType, content));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void adoptComment(Long id, Long topicId, Short topicType) {
        Comment comment = this.findComment(id);
        if (Objects.equal(comment.getState(), SysConst.CommentState.ADOPT.getType())) {
            throw new OperationException("已经被采纳了");
        }
        List<Comment> commentList = commentRepository.findByTopicIdAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        List<Comment> dbComment = commentList.parallelStream()
                .peek(ct -> {
                    if (Objects.equal(ct.getId(), id)) {
                        ct.setState(SysConst.CommentState.ADOPT.getType());
                    } else {
                        ct.setState(SysConst.CommentState.NOT_ADOPTED.getType());
                    }
                })
                .collect(toList());
        commentRepository.saveAll(dbComment);
    }

    @Override
    public Comment findComment(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public JSONObject countComment(Long topicId, Short topicType) {
        long count = commentRepository.countByTopicIdAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        JSONObject result = new JSONObject();
        result.put("commentNum", count);
        return result;
    }

    @Override
    public Map<Long, Long> countComment(List<Long> topicId, Short topicType) {
        List<Comment> commentList = commentRepository.findByTopicIdInAndTopicTypeAndDeleteState(topicId, topicType, UN_DELETED);
        return commentList.stream().collect(groupingBy(Comment::getTopicId, counting()));
    }

    @Override
    public PageImpl<CommentStatusOutputDTO> findCommentStatusDtoPage(Comment comment, Long userId) {
        Page<Comment> commentPage = this.findPageByEntity(comment);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, Collections.emptyList());
    }

    @Override
    public PageImpl<CommentStatusOutputDTO> findCommentAndReplyStatusDtoPage(Comment comment, Long userId) {
        Page<Comment> commentPage = this.findPageByEntity(comment);
        List<Long> commentIdList = commentPage.stream().map(Comment::getId).collect(toList());
        List<CommentReplyStatusOutputDTO> roCommentReplyStatusList = commentReplyService.findCommentReplyStatusDtoList(commentIdList);
        return this.resultRoCommentStatus(commentPage, comment.getTopicType(), userId, roCommentReplyStatusList);

    }

    private TopicCommentMapOutputDTO getTopicCommentMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, UserOutputDTO> roUserMap = userService.findMapUserDtoByUserId(userIdList);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_COMMENT);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_COMMENT);
        return new TopicCommentMapOutputDTO(roUserMap, zanNumMap, zanStateMap);
    }

    private PageImpl<CommentStatusOutputDTO> resultRoCommentStatus(Page<Comment> page, Short topicTyp, Long userId, List<CommentReplyStatusOutputDTO> roCommentReplyStatusList) {
        List<Long> topicId = page.stream().map(Comment::getId).collect(toList());
        List<Long> userIdList = page.stream().map(Comment::getUserId).distinct().collect(toList());
        TopicCommentMapOutputDTO topicCommentMap = getTopicCommentMethod(userIdList, userId, topicId, topicTyp);
        Map<Long, Long> commentReplyNumMap = roCommentReplyStatusList.stream()
                .collect(groupingBy(CommentReplyStatusOutputDTO::getCommentId, counting()));
        Map<Long, List<CommentReplyStatusOutputDTO>> roCommentReplyStatusMap = roCommentReplyStatusList.stream()
                .collect(groupingBy(CommentReplyStatusOutputDTO::getCommentId));
//        List<CommentStatusOutputDTO> roTransactionList = RoChangeEntityUtils
//                .resultRoCommentStatus(page.getContent(), userId, topicCommentMap, commentReplyNumMap, roCommentReplyStatusMap);
//        return new PageImpl<>(roTransactionList, page.getPageable(), page.getTotalElements());
        return new PageImpl<>(null, page.getPageable(), page.getTotalElements());
    }


    private List<SearchFilter> getCommentFilter(Comment comment) {
        List<SearchFilter> filters = Lists.newArrayList();
        filters.add(new SearchFilter("topicId", comment.getTopicId(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("topicType", comment.getTopicType(), SearchFilter.Operator.EQ));
        filters.add(new SearchFilter("deleteState", UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }

}
