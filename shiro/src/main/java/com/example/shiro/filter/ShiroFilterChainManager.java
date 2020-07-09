package com.example.shiro.filter;

import com.example.common.base.custom.SystemStatusCode;
import com.example.datasource.entity.Permission;
import com.example.shiro.service.PermissionService;
import com.example.shiro.service.UserService;
import com.example.shiro.redis.SecurityRedisService;
import com.example.shiro.token.JwtComponent;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 22:24
 * description: ShiroFilter 管理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class ShiroFilterChainManager {

    private final UserService userService;

    private final SecurityRedisService securityRedisService;

    private final JwtComponent jwtComponent;

    private final PermissionService permissionService;

    /**
     * 初始化获取过滤链
     */
    public Map<String, Filter> initGetFilters(CacheManager cacheManager) {
        Map<String, Filter> filters = Maps.newLinkedHashMap();
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setUserService(userService);
        jwtFilter.setSecurityRedisService(securityRedisService);
        jwtFilter.setJwtComponent(jwtComponent);
        filters.put("JwtFilter", jwtFilter);
        return filters;
    }

    /**
     * 初始化获取过滤链规则
     *
     * @return Map<String, String>
     */
    public Map<String, String> initGetFilterChain() {
        Map<String, String> filterChain = Maps.newLinkedHashMap();
        filterChain.put("/api/login/**", "anon");
        filterChain.put("/app/login/**", "anon");
        filterChain.put("/app/file/**", "anon");
        filterChain.put("/app/appVersion/**", "anon");

        filterChain.put("/api/logout/logout", "JwtFilter");
        filterChain.put("/app/logout/logout", "JwtFilter");

        List<Permission> permissionList = permissionService.findAll();
        for (Permission permission : permissionList) {
            if (StringUtils.isNotEmpty(permission.getPermission())) {
                filterChain.put(permission.getUrl(), "JwtFilter,perms[" + permission.getPermission() + "]");
            }
        }

        filterChain.put("/app/**", "JwtFilter,perms[" + SystemStatusCode.USER_NORMAL.getName() + "]");

        filterChain.put("/api/**", "JwtFilter,perms[" + SystemStatusCode.USER_NORMAL.getName() + "]");
        //   过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        return filterChain;
    }
}
