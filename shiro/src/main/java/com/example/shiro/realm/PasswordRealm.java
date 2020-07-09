package com.example.shiro.realm;

import com.example.common.utils.MD5Utils;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.User;
import com.example.shiro.service.UserService;
import com.example.datasource.utils.UserUtils;
import com.example.shiro.properties.AccountProperties;
import com.example.shiro.redis.SecurityRedisService;
import com.example.shiro.token.PasswordToken;
import com.google.common.base.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:37
 * description:
 */
@Slf4j
public class PasswordRealm extends AuthorizingRealm {

    @Setter
    private UserService userService;

    @Setter
    private AccountProperties accountProperties;

    @Setter
    private SecurityRedisService securityRedisService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken)) {
            return null;
        }
        PasswordToken token = (PasswordToken) authenticationToken;
        String loginType = token.getLoginType();
        if (Objects.equal(SysConst.LoginType.AJAX.getType(), loginType)) {
            return getAjaxAuthenticationInfo(token);
        }
        if (Objects.equal(SysConst.LoginType.TOKEN.getType(), loginType)) {
            return getTokenAuthenticationInfo(token);
        }
        if (Objects.equal(SysConst.LoginType.VERIFICATION_CODE.getType(), loginType)) {
            return getVerificationCodeAuthenticationInfo(token);
        }
        throw new AccountException("非法登陆！");
    }

    private AuthenticationInfo getAjaxAuthenticationInfo(PasswordToken token) {
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());

        boolean accountLockState = securityRedisService.getAccountLockState(username);
        if (accountLockState) {
            throw new DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录，请稍后重新尝试。");
        }

        //密码进行加密处理  明文为  password+name
        Optional<User> userOptional = userService.findOptByAccount(username);
        if (!userOptional.isPresent()) {
            //登录错误开始计数
            increment(username);
            throw new AccountException("账户不存在！");
        }
        User sysUser = userOptional.get();
        String userState = UserUtils.checkUserState(sysUser);
        if (StringUtils.isNotBlank(userState)) {
            //登录错误开始计数
            increment(username);
            throw new AccountException(userState);
        }
        String pawDes = UserUtils.getEncryptPassword(username, password, sysUser.getSalt());
        // 从数据库获取对应用户名密码的用户
        String sysUserPassword = sysUser.getPassword();
        if (!Objects.equal(sysUserPassword, pawDes)) {
            //登录错误开始计数
            increment(username);
            throw new AccountException("帐号或密码错误！");
        } else {
            //登录成功
            //更新登录时间 last login time
            userService.updateLastLoginTime(sysUser.getId());
            //清空登录计数
            securityRedisService.delAccountLoginCount(username);
            securityRedisService.delAccountLockState(username);
        }
        log.info("身份认证成功，登录用户：" + username);
        return new SimpleAuthenticationInfo(sysUser, password, getName());
    }

    private AuthenticationInfo getTokenAuthenticationInfo(PasswordToken token) {
        String username = token.getUsername();
        String apiToken = token.getToken();
        String apiTime = token.getTime();
        Optional<User> userOptional = userService.findOptByAccount(username);
        if (!userOptional.isPresent()) {
            throw new AccountException("账户不存在！");
        }
        User sysUser = userOptional.get();
        String userState = UserUtils.checkUserState(sysUser);
        if (StringUtils.isNotBlank(userState)) {
            throw new AccountException(userState);
        }
        String password = sysUser.getPassword();
        String mToken = MD5Utils.MD5(username + password + apiTime);
        if (Objects.equal(apiToken, mToken)) {
            log.info("身份认证成功，登录用户：" + username);
            return new SimpleAuthenticationInfo(sysUser, "888888", getName());
        } else {
            throw new AccountException("token值不正确！");
        }
    }

    private AuthenticationInfo getVerificationCodeAuthenticationInfo(PasswordToken token) {
        String username = token.getUsername();
        String noticeType = token.getNoticeType();
        boolean accountLockState = securityRedisService.getAccountLockState(username);
        if (accountLockState) {
            throw new DisabledAccountException("你正在尝试非法登录，帐号已经禁止登录，请稍后重新尝试。");
        }

        Optional<User> userOptional = Optional.empty();
        if (Objects.equal(noticeType, SysConst.NoticeType.PHONE.getType())) {
            userOptional = userService.findOptByPhone(username);
        }
        if (!userOptional.isPresent()) {
            increment(username);
            throw new AccountException("手机号不存在！");
        }
        User sysUser = userOptional.get();
        String userState = UserUtils.checkUserState(userOptional.get());
        if (StringUtils.isNotBlank(userState)) {
            increment(username);
            throw new AccountException(userState);
        }
        String verificationCode = token.getVerificationCode();
//            boolean state = verificationCodeService.validateCode(name, noticeType, verificationCode, SysConst.VerificationCodeType.LOGIN.getType());
        if (true) {
            //登录成功
            //更新登录时间 last login time
            userService.updateLastLoginTime(sysUser.getId());
            //清空登录计数
            securityRedisService.delAccountLoginCount(username);
            securityRedisService.delAccountLockState(username);
//                verificationCodeService.deleteCode(name, noticeType, verificationCode, SysConst.VerificationCodeType.LOGIN.getType());
            log.info("身份认证成功，登录用户：" + username);
            return new SimpleAuthenticationInfo(sysUser, "888888", getName());
        } else {
            //登录错误开始计数
            increment(username);
            throw new AccountException("验证码值不正确！");
        }
    }

    private void increment(String account) {
        //访问一次，计数一次
        securityRedisService.incrAccountLoginCount(account);
        //计数大于5时，设置用户被锁定10分钟
        securityRedisService.getAccountLoginCount(account).ifPresent(accountLoginCount -> {
            Integer firstErrorAccountErrorCount = accountProperties.getFirstErrorAccountErrorCount();
            Integer secondErrorAccountErrorCount = accountProperties.getSecondErrorAccountErrorCount();
            Integer thirdErrorAccountErrorCount = accountProperties.getThirdErrorAccountErrorCount();

            if (accountLoginCount.equals(firstErrorAccountErrorCount)) {
                securityRedisService.setAccountLockState(account, accountProperties.getFirstErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (accountLoginCount.equals(secondErrorAccountErrorCount)) {
                securityRedisService.setAccountLockState(account, accountProperties.getSecondErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (accountLoginCount >= thirdErrorAccountErrorCount) {
                securityRedisService.setAccountLockState(account, accountProperties.getThirdErrorAccountLockTime(), TimeUnit.HOURS);
            }
        });
    }
}
