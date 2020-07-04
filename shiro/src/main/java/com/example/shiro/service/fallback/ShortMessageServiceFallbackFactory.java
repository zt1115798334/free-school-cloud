package com.example.shiro.service.fallback;

import com.example.shiro.base.BaseController;
import com.example.shiro.base.ResultMessage;
import com.example.shiro.service.ShortMessageService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/4/28 13:12
 * description:
 */
@Component
public class ShortMessageServiceFallbackFactory extends BaseController implements FallbackFactory<ShortMessageService> {
    @Override
    public ShortMessageService create(Throwable throwable) {
        return (phoneNumbers, code, codeType) -> failure();
    }
}
