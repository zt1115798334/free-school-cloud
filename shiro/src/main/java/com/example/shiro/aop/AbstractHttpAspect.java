package com.example.shiro.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.common.utils.MStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 9:41
 * description:
 */
@Slf4j
public abstract class AbstractHttpAspect {

    /**
     * 方法名称
     */
    private static final ThreadLocal<String> METHOD_NAME = new ThreadLocal<>();
    /**
     * 参数信息
     */
    private static final ThreadLocal<String> PARAM_VAL = new ThreadLocal<>();
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    /**
     * aopPointCut
     */
    protected abstract void aopPointCut();

    @Before("aopPointCut()")
    private void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        METHOD_NAME.set(signature.getDeclaringTypeName() + "." + signature.getName());

        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        PARAM_VAL.set(MStringUtils.parseParams(parameterNames, parameterValues));
        START_TIME.set(System.currentTimeMillis());
    }

    @After("aopPointCut()")
    private void doAfter() {
        long time = System.currentTimeMillis() - START_TIME.get();
        log.info("执行 " + METHOD_NAME + " 耗时为：" + time + "ms");
        log.info("参数信息：" + PARAM_VAL);
        METHOD_NAME.remove();
        PARAM_VAL.remove();
        START_TIME.remove();
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut()")
    private void doAfterReturning(Object object) {
        if (object != null) {
            if (object instanceof JSONObject) {
                JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(object));
                result.remove("data");
                log.info("response：{}", result.toJSONString());
            } else {
                log.info("response：{}", object.toString());
            }
        }
    }

}
