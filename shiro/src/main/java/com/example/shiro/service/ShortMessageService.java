package com.example.shiro.service;

import com.example.shiro.base.ResultMessage;
import com.example.shiro.service.fallback.ShortMessageServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/3 10:36
 * description:
 */

@FeignClient(name = "SERVICE-SHORT-MESSAGE",
        fallbackFactory = ShortMessageServiceFallbackFactory.class)
public interface ShortMessageService {

    /**
     * 发送验证码
     *
     * @param phoneNumbers 手机号
     * @param code         验证码
     * @param codeType     验证码类型
     * @return 结果
     */
    @PostMapping(value = "sendShortMessageFromCode",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResultMessage sendShortMessageFromCode(@RequestParam String phoneNumbers,
                                           @RequestParam String code,
                                           @RequestParam String codeType);
}
