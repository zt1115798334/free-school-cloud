package com.example.serviceadditional.service.impl;

import com.example.common.dto.output.InformationOutputDTO;
import com.example.common.dto.output.TopicMapOutputDTO;
import com.example.common.dto.output.UserOutputDTO;
import com.example.datasource.dto.DtoUtils;
import com.example.datasource.entity.Information;
import com.example.serviceadditional.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author zhang
 */
@AllArgsConstructor
@Component
public class TopicServiceImpl implements TopicService {

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final ZanService zanService;

    private final UserService userService;

    private final CollectionService collectionService;

    private TopicMapOutputDTO getTopicMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, UserOutputDTO> userMap = userService.findMapUserDtoByUserId(userIdList);
        Map<Long, List<Long>> topicImgMap = topicImgService.findTopicImgList(topicId, topicTyp);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_TOPIC);
        Map<Long, List<UserOutputDTO>> zanUserMap = zanService.zanUser(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> commentCountMap = commentService.countComment(topicId, topicTyp);
        Map<Long, Long> collectionStateMap = collectionService.collectionState(userId, topicId, topicTyp);
        return new TopicMapOutputDTO(userMap, topicImgMap, zanNumMap, zanStateMap, collectionStateMap, commentCountMap, zanUserMap);
    }

    @Override
    public InformationOutputDTO resultInformationDto(Information information, Long userId) {
        List<Long> topicId = Collections.singletonList(information.getId());
        TopicMapOutputDTO topicMethod = getTopicMethod(Collections.singletonList(information.getUserId()), userId, topicId, TOPIC_TYPE_2);
        return DtoUtils.convertToInformationOutputDTO(information, userId, topicMethod);

    }

    @Override
    public List<InformationOutputDTO> resultInformationDtoList(List<Information> list, Long userId) {
        List<Long> topicId = list.stream().map(Information::getId).collect(toList());
        List<Long> userIdList = list.stream().map(Information::getUserId).distinct().collect(toList());
        TopicMapOutputDTO topicMethod = getTopicMethod(userIdList, userId, topicId, TOPIC_TYPE_2);
        return DtoUtils.convertToInformationOutputDTO(list, userId, topicMethod);
    }
}
