package com.example.shiro.realm;

import com.example.shiro.service.PermissionService;
import com.example.shiro.service.UserService;
import com.example.shiro.properties.AccountProperties;
import com.example.shiro.redis.SecurityRedisService;
import com.example.shiro.token.JwtComponent;
import com.example.shiro.token.JwtToken;
import com.example.shiro.token.PasswordToken;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:26
 * description: realm管理器
 */
@AllArgsConstructor
@Component
public class RealmManager {

    private final UserService userService;

    private final AccountProperties accountProperties;

    private final SecurityRedisService securityRedisService;

    private final JwtComponent jwtComponent;

    private final PermissionService permissionService;

    public List<Realm> initGetRealm() {
        List<Realm> realmList = Lists.newArrayList();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setAccountProperties(accountProperties);
        passwordRealm.setSecurityRedisService(securityRedisService);
//        passwordRealm.setVerificationCodeService(verificationCodeService);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setUserService(userService);
        jwtRealm.setJwtComponent(jwtComponent);
        jwtRealm.setSecurityRedisService(securityRedisService);
        jwtRealm.setPermissionService(permissionService);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return Collections.unmodifiableList(realmList);
    }
}
