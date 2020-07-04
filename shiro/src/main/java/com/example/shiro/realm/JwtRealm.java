package com.example.shiro.realm;

import com.example.common.base.custom.SystemStatusCode;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.Permission;
import com.example.datasource.entity.User;
import com.example.datasource.service.PermissionService;
import com.example.datasource.service.UserService;
import com.example.datasource.utils.UserUtils;
import com.example.redissource.service.redis.StringRedisService;
import com.example.shiro.redis.SecurityRedisService;
import com.example.shiro.token.JwtToken;
import com.example.shiro.token.JwtComponent;
import com.example.shiro.utils.NetworkUtil;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:41
 * description: 进行授权 以及 jwt的验证
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Setter
    private JwtComponent jwtComponent;
    @Setter
    private UserService userService;
    @Setter
    private SecurityRedisService securityRedisService;
    @Setter
    private PermissionService permissionService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("开始执行权限认证...");
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> permissionSet = Sets.newHashSet();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user != null) {
            if (UserUtils.checkUserFrozen(user)) {
                permissionSet.add(SystemStatusCode.USER_FROZEN.getName());
            } else if (Objects.equal(user.getDeleteState(), SysConst.DeleteState.DELETE.getCode())) {
                permissionSet.add(SystemStatusCode.USER_DELETE.getName());
            } else {
                permissionSet.add(SystemStatusCode.USER_NORMAL.getName());
                //添加系统权限
                List<Permission> permissionList = permissionService.findByUserId(user.getId());
                Set<String> collect = permissionList.stream()
                        .map(Permission::getPermission)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
                permissionSet.addAll(collect);
            }

        } else {
            permissionSet.add(SystemStatusCode.USER_NOT_FOUND.getName());
        }
        info.setStringPermissions(permissionSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        开始验证token值的正确...
        if (!(authenticationToken instanceof JwtToken)) {
            return null;
        }
        JwtToken jwtToken = (JwtToken) authenticationToken;

        String token = (String) jwtToken.getCredentials();
        String deviceInfo = jwtToken.getDeviceInfo();
        Long userId = jwtToken.getUserId();
        String ip = jwtToken.getIpHost();
        Long ipLong = Objects.equal(deviceInfo, SysConst.DeviceInfo.WEB.getType()) ?
                NetworkUtil.ipToLong(ip) : 0L;
        if (StringUtils.isNotBlank(token)) {
            if (userId != null) {
                Optional<String> accessTokenRedis =securityRedisService.getAccountAccessToken(deviceInfo,userId,ipLong);
                if (accessTokenRedis.isPresent()) {
                    if (Objects.equal(accessTokenRedis.get(), token)) {
                        Optional<User> userOptional = userService.findByIdNotDelete(userId);
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            if (jwtComponent.validateToken(token, user)) {
                                return new SimpleAuthenticationInfo(user, token, getName());
                            } else {
                                throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                            }
                        } else {
                            throw new AuthenticationException(SystemStatusCode.USER_NOT_FOUND.getName());
                        }
                    } else {

                        throw new AuthenticationException(SystemStatusCode.JWT_DIFFERENT_PLACES.getName());
                    }
                } else {
                    throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                }
            } else {
                throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
            }
        }
        throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
    }
}
