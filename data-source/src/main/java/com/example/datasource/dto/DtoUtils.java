package com.example.datasource.dto;

import com.example.common.dto.input.CommentPageInputDTO;
import com.example.common.dto.input.PageInputDTO;
import com.example.common.dto.input.ParamsInputDTO;
import com.example.common.dto.input.StorageInformationInputDTO;
import com.example.common.dto.output.*;
import com.example.common.utils.DateUtils;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.entity.*;
import com.example.datasource.utils.UserUtils;
import com.google.common.base.Objects;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 14:43
 * description:
 */
public class DtoUtils {


    public static CustomPage convertToCustomPage(PageInputDTO inputDto) {
        Function<PageInputDTO, CustomPage> function = (PageInputDTO dto) -> {
            CustomPage result = new CustomPage(0, 0);
            BeanUtils.copyProperties(dto, result);
            return result;
        };
        return function.apply(inputDto);
    }

    public static Information convertToInformation(ParamsInputDTO inputDto) {
        Function<ParamsInputDTO, Information> function = (ParamsInputDTO dto) -> {
            Information result = new Information();
            BeanUtils.copyProperties(dto, result);
            return result;
        };
        return function.apply(inputDto);
    }

    public static Information convertToInformation(StorageInformationInputDTO inputDto) {
        Function<StorageInformationInputDTO, Information> function = (StorageInformationInputDTO dto) -> {
            Information result = new Information();
            BeanUtils.copyProperties(dto, result);
            return result;
        };
        return function.apply(inputDto);
    }

    public static Comment convertToComment(CommentPageInputDTO inputDto) {
        Function<CommentPageInputDTO, Comment> function = (CommentPageInputDTO dto) -> {
            Comment result = new Comment();
            BeanUtils.copyProperties(dto, result);
            return result;
        };
        return function.apply(inputDto);
    }


    public static UserOutputDTO convertToUserOutputDTO(User user, UserImg userImgUrl) {
        Function<User, UserOutputDTO> function = (User dto) -> {
            UserOutputDTO result = new UserOutputDTO();
            BeanUtils.copyProperties(dto, result);
            return result;
        };
        return function.apply(user).setUserImgUrl(new ImagePathOutputDTO(userImgUrl.getImgId()));
    }

    public static List<UserOutputDTO> convertToUserOutputDTO(List<User> userList, Map<Long, UserImg> userImgMap) {
        return userList.stream().map(user ->
                convertToUserOutputDTO(user,
                        userImgMap.getOrDefault(user.getId(), UserUtils.getDefaultUserImg())))
                .collect(toList());
    }

    public static InformationOutputDTO convertToInformationOutputDTO(Information information, Long userId,
                                                              TopicMapOutputDTO topicMethod) {
        Long informationId = information.getId();
        Long informationUserId = information.getUserId();
        return new InformationOutputDTO(
                topicMethod.getUserMap().getOrDefault(informationUserId, UserUtils.getDefaultUserDto()),
                information.getState(),
                Objects.equal(informationUserId, userId),
                topicMethod.getZanStateMap().containsKey(informationId),
                topicMethod.getZanNumMap().getOrDefault(informationId, 0L),
                topicMethod.getZanUserMap().getOrDefault(informationId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(informationId),
                topicMethod.getCommentCountMap().getOrDefault(informationId, 0L),
                information.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(informationId, Collections.emptyList())
                        .stream()
                        .map(ImagePathOutputDTO::new)
                        .collect(toList()),
                informationId,
                information.getTitle(),
                DateUtils.formatDateTime(information.getCreatedTime()),
                information.getDescribeContent());
    }

    public static List<InformationOutputDTO> convertToInformationOutputDTO(List<Information> informationList,
                                                          Long userId,
                                                          TopicMapOutputDTO topicMethod) {
        return informationList.stream()
                .map(information ->
                        convertToInformationOutputDTO(information, userId, topicMethod)).collect(toList());
    }
}
