package com.example.redissource.service.redis;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/13 10:48
 * description:
 */
public interface StringRedisService {

    /**
     * 增长值
     *
     * @param key   key值
     */
    void increment(String key);

    /**
     * 增长值
     *
     * @param key   key值
     * @param delta 增长值
     */
    void increment(String key, long delta);

    /**
     * 设置过期
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void expire(String key, String value, long timeout, TimeUnit unit);

    /**
     * setNotContainExpire
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpire(String key, String value);

    /**
     * setContainExpire 包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setContainExpire(String key, String value);

    /**
     * setNotContainExpire 包含过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void setContainExpire(String key, String value, long timeout, TimeUnit unit);

    /**
     * @param key key
     * @return Optional<String>
     */
    Optional<String> get(String key);

    /**
     * keys
     *
     * @param key key
     * @return Set<String>
     */
    Set<String> keys(String key);

    /**
     * flushDB
     */
    void flushDB();

    /**
     * dbSize
     */
    Long dbSize();

    /**
     * 删除
     *
     * @param key key
     * @return boolean
     */
    Boolean delete(String key);

    /**
     * 删除
     *
     * @param key key
     * @return boolean
     */
    Boolean deleteByLike(String key);
}
