package com.example.serviceadditional.service;

import com.example.common.dto.output.UserOutputDTO;
import com.example.serviceadditional.service.fallback.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 11:33
 * description:
 */
@FeignClient(name = "SERVICE-USER", fallbackFactory = UserServiceFallbackFactory.class, path = "service/user")
public interface UserService {

    /**
     * findMapUserDtoByUserId
     *
     * @param userIdList userIdList
     * @return Map
     */
    @PostMapping(value = "findMapUserDtoByUserId",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Map<Long, UserOutputDTO> findMapUserDtoByUserId(@RequestParam List<Long> userIdList);
}
