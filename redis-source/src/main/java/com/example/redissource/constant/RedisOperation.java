package com.example.redissource.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 14:44
 * description: redis操作
 */
@Getter
@AllArgsConstructor
public enum RedisOperation {
    /**
     * 设置指定 key 的值
     */
    SET,
    /**
     * 获取指定 key 的值
     */
    GET,
    /**
     * 删除指定 key 的值
     */
    DEL,
    /**
     * 将 key 中储存的数字值增一
     */
    INCR;
}
