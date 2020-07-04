package com.example.serviceuser.aop;

import com.example.shiro.aop.AbstractHttpAspect;
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
public class HttpAspect extends AbstractHttpAspect {
    /**
     * 切入点
     */
    @Override
    @Pointcut("execution( * com.example.school.app.controller..*.*(..))")
    public void aopPointCut() {

    }


}
