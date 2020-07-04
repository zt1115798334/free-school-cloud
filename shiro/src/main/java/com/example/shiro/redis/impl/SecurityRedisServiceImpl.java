package com.example.shiro.redis.impl;

import com.example.redissource.service.redis.StringRedisService;
import com.example.shiro.properties.JwtProperties;
import com.example.shiro.redis.SecurityRedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 16:04
 * description:
 */
@AllArgsConstructor
@Service
public class SecurityRedisServiceImpl implements SecurityRedisService {


    private final StringRedisService stringRedisService;

    private final JwtProperties jwtProperties;

    private final String ACCOUNT_LOGIN_COUNT = "security:accountLoginCount:";
    private final String ACCOUNT_LOCK_STATE = "security:accountLockState:";

    private final Function<String, String> accountLoginCountFun = (s -> ACCOUNT_LOGIN_COUNT + s);
    private final Function<String, String> accountLockStateFun = (s -> ACCOUNT_LOCK_STATE + s);

    private String accountAccessTokenFun(String deviceInfo, Long userId, Long ipLong) {
        String accountAccessToken = "security:accountAccessToken:";
        return accountAccessToken + deviceInfo + ":" + userId + ":" + ipLong;
    }

    private String accountRefreshTokenFun(String deviceInfo, Long userId, Long ipLong) {
        String accountRefreshToken = "security:accountRefreshToken:";
        return accountRefreshToken + deviceInfo + ":" + userId + ":" + ipLong;
    }

    @Override
    public Optional<Integer> getAccountLoginCount(String account) {
        return stringRedisService.get(accountLoginCountFun.apply(account)).map(Integer::valueOf);
    }

    @Override
    public void delAccountLoginCount(String account) {
        stringRedisService.delete(accountLoginCountFun.apply(account));
    }

    @Override
    public void incrAccountLoginCount(String account) {
        stringRedisService.increment(accountLoginCountFun.apply(account));
    }

    @Override
    public boolean getAccountLockState(String account) {
        return stringRedisService.get(accountLockStateFun.apply(account)).isPresent();
    }

    @Override
    public void delAccountLockState(String account) {
        stringRedisService.delete(accountLockStateFun.apply(account));
    }

    @Override
    public void setAccountLockState(String account, long timeout, TimeUnit unit) {
        stringRedisService.setContainExpire(accountLockStateFun.apply(account), "LOCK", timeout, unit);
    }

    @Override
    public Optional<String> getAccountAccessToken(String deviceInfo, Long userId, Long ipLong) {
        return stringRedisService.get(accountAccessTokenFun(deviceInfo, userId, ipLong));
    }

    @Override
    public void delAccountAccessToken(String deviceInfo, Long userId, Long ipLong) {
        stringRedisService.delete(accountAccessTokenFun(deviceInfo, userId, ipLong));
    }

    @Override
    public void setAccountAccessToken(String deviceInfo, Long userId, Long ipLong, String accessToken, boolean rememberMe) {
        stringRedisService.setContainExpire(accountAccessTokenFun(deviceInfo, userId, ipLong), accessToken, jwtProperties.getExpiration(), TimeUnit.HOURS);
    }

    @Override
    public Optional<String> getAccountRefreshToken(String deviceInfo, Long userId, Long ipLong) {
        return stringRedisService.get(accountRefreshTokenFun(deviceInfo, userId, ipLong));
    }

    @Override
    public void delAccountRefreshToken(String deviceInfo, Long userId, Long ipLong) {
        stringRedisService.delete(accountRefreshTokenFun(deviceInfo, userId, ipLong));
    }

    @Override
    public void setAccountRefreshToken(String deviceInfo, Long userId, Long ipLong, String refreshToken, boolean rememberMe) {
        stringRedisService.setContainExpire(accountRefreshTokenFun(deviceInfo, userId, ipLong), refreshToken, jwtProperties.getRefreshExpiration(), TimeUnit.HOURS);
    }

}
