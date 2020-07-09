package com.example.serviceadditional.service.fallback;

import com.example.serviceadditional.service.UserService;
import com.google.common.collect.Maps;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 11:34
 * description:
 */
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {

    @Override
    public UserService create(Throwable throwable) {
        return (userIdList) -> Maps.newHashMap();
    }
}
