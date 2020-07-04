package com.example.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.example.common.base.custom.SystemStatusCode;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.User;
import com.example.datasource.service.UserService;
import com.example.shiro.base.ResultMessage;
import com.example.shiro.redis.SecurityRedisService;
import com.example.shiro.token.JwtComponent;
import com.example.shiro.token.JwtToken;
import com.example.shiro.utils.NetworkUtil;
import com.example.shiro.utils.RequestResponseUtil;
import com.google.common.base.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:12
 * description:
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Setter
    private UserService userService;
    @Setter
    private SecurityRedisService securityRedisService;
    @Setter
    private JwtComponent jwtComponent;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return (null != subject && subject.isAuthenticated());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        try {
            AuthenticationToken token = createJwtToken(request);
            this.getSubject(request, response).login(token);
            return true;
        } catch (AuthenticationException e) {
            String exceptionMsg = e.getMessage();
            if (Objects.equal(exceptionMsg, SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName())) {
//                判断RefreshToken未过期就进行AccessToken刷新
                if (!this.refreshToken(request, response)) {
                    //jwt 已过期,通知客户端重新登录
                    ResultMessage message = new ResultMessage().ok(SystemStatusCode.JWT_EXPIRE.getCode(), SystemStatusCode.JWT_EXPIRE.getName());
                    RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                }
            }
            if (Objects.equal(exceptionMsg, SystemStatusCode.JWT_NOT_FOUND.getName())) {
                ResultMessage message = new ResultMessage().ok(SystemStatusCode.JWT_NOT_FOUND.getCode(), SystemStatusCode.JWT_NOT_FOUND.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
            if (Objects.equal(exceptionMsg, SystemStatusCode.JWT_DIFFERENT_PLACES.getName())) {
                ResultMessage message = new ResultMessage().ok(SystemStatusCode.JWT_DIFFERENT_PLACES.getCode(), SystemStatusCode.JWT_DIFFERENT_PLACES.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
            if (Objects.equal(exceptionMsg, SystemStatusCode.USER_NOT_FOUND.getName())) {
                ResultMessage message = new ResultMessage().ok(SystemStatusCode.USER_NOT_FOUND.getCode(), SystemStatusCode.USER_NOT_FOUND.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
        }

        return false;
        // 过滤链终止
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {

        Map<String, String> maps = RequestResponseUtil.getRequestHeaders(request);
        String ipHost = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        String deviceInfo = maps.get("deviceInfo".toLowerCase());
        String token = maps.get("authorization");
        Long userId = jwtComponent.getUserIdFromToken(token);
        return new JwtToken(userId, ipHost, deviceInfo, token);
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     *
     * @param request  request
     * @param response response
     * @return ResultMessage
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        Map<String, String> maps = RequestResponseUtil.getRequestHeaders(request);
        String deviceInfo = maps.get("deviceInfo".toLowerCase());
        String token = maps.get("authorization");
        Long userId = jwtComponent.getUserIdFromToken(token);

        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        Long ipLong = Objects.equal(deviceInfo, SysConst.DeviceInfo.WEB.getType()) ?
                NetworkUtil.ipToLong(ip) : 0L;

        Optional<String> refreshTokenOption = securityRedisService.getAccountRefreshToken(deviceInfo, userId, ipLong);
        if (refreshTokenOption.isPresent()) {

            Optional<User> userOptional = userService.findByIdNotDelete(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String accessToken = jwtComponent.generateAccessToken(user);
                String refreshToken = jwtComponent.generateRefreshToken(user);
                //token 存储redis
                securityRedisService.setAccountAccessToken(deviceInfo,userId,ipLong,accessToken, Boolean.FALSE);
                securityRedisService.setAccountRefreshToken(deviceInfo,userId,ipLong,refreshToken, Boolean.FALSE);
                //发送新的token
                ResultMessage message = new ResultMessage()
                        .ok(SystemStatusCode.NEW_JWT.getCode(), SystemStatusCode.NEW_JWT.getName())
                        .addData("accessToken", accessToken);
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return true;
            }
        }
        return false;
    }
}
