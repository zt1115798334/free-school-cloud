package com.example.serviceshortmessage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 14:44
 * description:
 */
@Getter
@AllArgsConstructor
public enum VerificationCodeType {
    /**
     * 账户注册
     */
    REGISTER("register", "账户注册"),
    /**
     * 账户登陆
     */
    LOGIN("login", "账户登陆"),
    /**
     * 密码重置
     */
    FORGET("forget", "密码重置");


    private final String type;

    private final String name;
}
