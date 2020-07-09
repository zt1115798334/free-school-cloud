package com.example.serviceuser.aop;

import com.example.shiro.service.UserLogService;
import com.example.shiro.aop.AbstractHttpAspectSave;
import com.example.shiro.aop.SaveLog;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:29
 * description:
 */
@Aspect
@Component
public class HttpAspectSave extends AbstractHttpAspectSave {


    public HttpAspectSave(UserLogService userLogService) {
        super(userLogService);
    }

    /**
     * 切入点
     */
    @Override
    @Pointcut("execution( * com.example.serviceuser.controller..*.*(..)) && @annotation(logs)")
    public void aopPointCut(SaveLog logs) {

    }


}
