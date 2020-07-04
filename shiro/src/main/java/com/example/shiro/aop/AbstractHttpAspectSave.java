package com.example.shiro.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.common.exception.custom.OperationException;
import com.example.common.utils.DateUtils;
import com.example.common.utils.MStringUtils;
import com.example.datasource.entity.User;
import com.example.datasource.entity.UserLog;
import com.example.datasource.service.UserLogService;
import com.example.shiro.base.CurrentUser;
import com.example.shiro.utils.NetworkUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 9:49
 * description:
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractHttpAspectSave implements CurrentUser {
    /**
     * 类名
     */
    private static final ThreadLocal<String> CLASS_NAME = new ThreadLocal<>();
    /**
     * 方法名
     */
    private static final ThreadLocal<String> METHOD_NAME = new ThreadLocal<>();
    /**
     * 参数信息
     */
    private static final ThreadLocal<String> PARAM_VAL = new ThreadLocal<>();
    /**
     * ip地址
     */
    private static final ThreadLocal<String> IP = new ThreadLocal<>();

    private final UserLogService userLogService;

    /**
     * aopPointCut
     * @param logs logs
     */
    protected abstract void aopPointCut(SaveLog logs);

    @Before(value = "aopPointCut(logs)", argNames = "joinPoint,logs")
    private void doBefore(JoinPoint joinPoint, SaveLog logs) {
        Signature signature = joinPoint.getSignature();
        CLASS_NAME.set(signature.getDeclaringTypeName());
        METHOD_NAME.set(signature.getName());
        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        PARAM_VAL.set(MStringUtils.parseParams(parameterNames, parameterValues));
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        IP.set(NetworkUtil.getLocalIp(request));
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut(logs)", argNames = "object,logs")
    private void doAfterReturning(Object object, SaveLog logs) throws OperationException {
        //返回值
        String response = JSONObject.toJSONString(object);
        String desc = logs.desc();
        User currentUser = getCurrentUser();
        Long userId = null;
        String username = null;
        if (currentUser != null) {
            userId = currentUser.getId();
            username = currentUser.getUserName();
        }
        UserLog userLog = new UserLog(userId, username, desc, PARAM_VAL.get(), IP.get(), DateUtils.currentDateTime(), CLASS_NAME.get(), METHOD_NAME.get(), response);
        userLogService.save(userLog);
        CLASS_NAME.remove();
        METHOD_NAME.remove();
        PARAM_VAL.remove();
        IP.remove();

    }
}
