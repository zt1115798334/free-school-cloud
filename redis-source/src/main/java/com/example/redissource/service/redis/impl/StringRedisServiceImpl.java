package com.example.redissource.service.redis.impl;

import com.example.redissource.service.redis.StringRedisService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

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
@AllArgsConstructor
@Component
public class StringRedisServiceImpl implements StringRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void increment(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
//        valueOperations.increment(key);
    }

    @Override
    public void increment(String key, long delta) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.increment(key, delta);
    }

    @Override
    public void expire(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
        stringRedisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void setNotContainExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);

    }

    @Override
    public void setContainExpire(String key, String value) {
        setContainExpire(key, value, 30, TimeUnit.MINUTES);
    }

    @Override
    public void setContainExpire(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public Optional<String> get(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String s = valueOperations.get(key);
        return Optional.ofNullable(s);
    }

    @Override
    public Set<String> keys(String key) {
        return stringRedisTemplate.keys(key);
    }

    @Override
    public void flushDB() {
        stringRedisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    @Override
    public Long dbSize() {
        return stringRedisTemplate.execute(RedisServerCommands::dbSize);
    }

    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public Boolean deleteByLike(String key) {
        Set<String> keys = this.keys(key + "*");
        stringRedisTemplate.delete(keys);
        return true;
    }
}
