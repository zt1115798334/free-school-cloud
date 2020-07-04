package com.example.shiro.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 16:04
 * description:
 */
public interface SecurityRedisService {

    Optional<Integer> getAccountLoginCount(String account);

    void delAccountLoginCount(String account);

    void incrAccountLoginCount(String account);

    boolean getAccountLockState(String account);

    void delAccountLockState(String account);

    void setAccountLockState(String account, long timeout, TimeUnit unit);

    Optional<String> getAccountAccessToken(String deviceInfo, Long userId, Long ipLong);

    void delAccountAccessToken(String deviceInfo, Long userId, Long ipLong);

    void setAccountAccessToken(String deviceInfo, Long userId, Long ipLong, String accessToken, boolean rememberMe);

    Optional<String> getAccountRefreshToken(String deviceInfo, Long userId, Long ipLong);

    void delAccountRefreshToken(String deviceInfo, Long userId, Long ipLong);

    void setAccountRefreshToken(String deviceInfo, Long userId, Long ipLong, String refreshToken,boolean rememberMe);
}
