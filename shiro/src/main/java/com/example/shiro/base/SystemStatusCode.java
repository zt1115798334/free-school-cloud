package com.example.shiro.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/21 13:55
 * description: 状态码
 */
@AllArgsConstructor
@Getter
public enum SystemStatusCode {

    /**
     * 成功
     */
    SUCCESS(0, "success", "成功"),
    /**
     * 失败
     */
    FAILED(1, "failed", "失败"),
    /**
     * 参数解析失败
     */
    SC_BAD_REQUEST(400, "badRequest", "参数解析失败"),
    /**
     * 不支持当前请求方
     */
    SC_METHOD_NOT_ALLOWED(405, "methodNotAllowed", "不支持当前请求方"),
    /**
     * 不支持当前媒体类型
     */
    SC_UNSUPPORTED_MEDIA_TYPE(415, "unsupportedMediaType", "不支持当前媒体类型"),
    /**
     * 系统错误
     */
    SC_INTERNAL_SERVER_ERROR(500, "internalServerError", "系统错误");
    private final Integer code;
    private final String name;
    private final String msg;
    }
