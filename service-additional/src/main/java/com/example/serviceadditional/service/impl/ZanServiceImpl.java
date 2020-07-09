package com.example.serviceadditional.service.impl;

import com.example.common.dto.output.UserOutputDTO;
import com.example.datasource.entity.Zan;
import com.example.datasource.repo.ZanRepository;
import com.example.datasource.utils.UserUtils;
import com.example.serviceadditional.service.UserService;
import com.example.serviceadditional.service.ZanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 20:11
 * description:
 */
@AllArgsConstructor
@Service
public class ZanServiceImpl implements ZanService {

    private final ZanRepository zanRepository;

    private final UserService userService;

//    private final JPushTool jPushTool;

    @Override
    public Map<Long, Long> zanState(Long userId, List<Long> topicId, Short topicType, Short zanType) {
        List<Zan> zanList = zanRepository.findByUserIdAndTopicIdInAndTopicTypeAndZanTypeAndZanState(userId, topicId, topicType, zanType, ON);
        return zanList.stream().collect(groupingBy(Zan::getTopicId, counting()));
    }

    @Override
    public Map<Long, Long> countZan(List<Long> topicId, Short topicType, Short zanType) {
        List<Zan> zanList = zanRepository.findByTopicIdInAndTopicTypeAndZanTypeAndZanState(topicId, topicType, zanType, ON);
        return zanList.stream().collect(groupingBy(Zan::getTopicId, counting()));
    }

    @Override
    public Map<Long, List<UserOutputDTO>> zanUser(List<Long> topicId, Short topicType, Short zanType) {
        List<Zan> zanList = zanRepository.findByTopicIdInAndTopicTypeAndZanTypeAndZanState(topicId, topicType, zanType, ON);
        List<Long> userIdList = zanList.stream().map(Zan::getUserId).distinct().collect(toList());
        Map<Long, UserOutputDTO> userMap = userService.findMapUserDtoByUserId(userIdList);
        return zanList.stream()
                .collect(groupingBy(Zan::getTopicId,
                        mapping(zan -> userMap.getOrDefault(zan.getUserId(), UserUtils.getDefaultUserDto()),
                                toList())));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void enableOnZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId) {
        Optional<Zan> zanOptional = zanRepository.findByUserIdAndTopicIdAndTopicTypeAndZanType(toUserId, topicId, topicType, zanType);
        Zan zan = zanOptional.orElse(new Zan(toUserId, topicId, topicType, zanType, ON));
        zan.setZanState(ON);
        zanRepository.save(zan);
//        jPushTool.pushZanInfo(topicId, topicType, zanType, toUserId, fromUserId);
    }

    @Override
    public void enableOffZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId) {
        Optional<Zan> zanOptional = zanRepository.findByUserIdAndTopicIdAndTopicTypeAndZanType(toUserId, topicId, topicType, zanType);
        Zan zan = zanOptional.orElse(new Zan(toUserId, topicId, topicType, zanType, OFF));
        zan.setZanState(OFF);
        zanRepository.save(zan);
    }
}
