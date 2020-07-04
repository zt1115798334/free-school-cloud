package com.example.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:39
 * description:
 */
@Setter
@Getter
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {

    /**
     * 用户的标识
     */

    private final Long userId;
    /**
     * 用户的IP
     */
    private final String ipHost;
    /**
     * 设备信息
     */
    private final String deviceInfo;
    /**
     * json web token值
     */
    private final String token;

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
