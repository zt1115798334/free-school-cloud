package com.example.serviceadditional.service;


import com.example.common.dto.output.UserOutputDTO;
import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.Zan;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 20:11
 * description:
 */
public interface ZanService extends BaseService<Zan, Long> {

    Map<Long, Long> zanState(Long userId, List<Long> topicId, Short topicType, Short zanType);

    Map<Long, Long> countZan(List<Long> topicId, Short topicType, Short zanType);

    Map<Long, List<UserOutputDTO>> zanUser(List<Long> topicId, Short topicType, Short zanType);

    void enableOnZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId);

    void enableOffZan(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId);
}
