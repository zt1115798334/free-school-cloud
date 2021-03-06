package com.example.serviceuser.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.common.exception.custom.OperationException;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.User;
import com.example.shiro.service.UserService;
import com.example.serviceuser.service.CommonLoginService;
import com.example.serviceuser.validation.NoticeType;
import com.example.shiro.aop.DistributedLock;
import com.example.shiro.aop.SaveLog;
import com.example.shiro.base.BaseController;
import com.example.shiro.base.ResultMessage;
import com.example.shiro.service.VerificationCodeService;
import com.example.shiro.token.PasswordToken;
import com.example.shiro.utils.NetworkUtil;
import com.example.shiro.utils.RequestResponseUtil;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/28 17:38
 * description: 登录接口
 */
@AllArgsConstructor
@RestController
@RequestMapping("app/login")
public class LoginController extends BaseController {

    private final CommonLoginService commonLoginService;

    private final UserService userService;

    private final VerificationCodeService verificationCodeService;

    private static final String DEVICE_INFO = SysConst.DeviceInfo.MOBILE.getType();

    /**
     * 登录
     *
     * @param username       用户名
     * @param password       密码
     * @param registrationId 极光推送，用户设备标识
     * @return ResultMessage
     */

    @PostMapping(value = "login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "app登录")
    @DistributedLock
    public ResultMessage login(HttpServletRequest request,
                               @NotBlank(message = "账户不能为空") @RequestParam String username,
                               @NotBlank(message = "密码不能为空") @RequestParam String password,
                               @NotBlank(message = "用户设备标识不能为空") @RequestParam String registrationId) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String loginType = SysConst.LoginType.AJAX.getType();
            PasswordToken token = new PasswordToken(username, password, loginType);
            String accessToken = commonLoginService.login(token, Boolean.TRUE, ip, DEVICE_INFO, registrationId);
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            result.put("accountType", user.getAccountType());
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }


    /**
     * 验证码登录
     *
     * @param request          request
     * @param noticeContent    通知内容
     * @param verificationCode 验证码
     * @param noticeType       通知类型{@link SysConst.NoticeType}
     * @return ResultMessage
     */
    @PostMapping(value = "verificationCodeLogin")
    @SaveLog(desc = "验证码登录")
    public ResultMessage verificationCodeLogin(HttpServletRequest request,
                                               @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                               @NotBlank(message = "验证码不能为空") @RequestParam(value = "verificationCode") String verificationCode,
                                               @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            String loginType = SysConst.LoginType.VERIFICATION_CODE.getType();
            PasswordToken token = new PasswordToken(noticeContent, "888888", true, verificationCode, noticeType, loginType);
            String accessToken = commonLoginService.login(token, true, ip, DEVICE_INFO);
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            return success("登录成功", result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    @PostMapping(value = "sendVerificationCodeByLogin")
    @SaveLog(desc = "发送验证码执行登录操作")
    public ResultMessage sendVerificationCodeByLogin(HttpServletRequest request,
                                                     @NotBlank(message = "手机号或者邮件不能为空") @RequestParam(value = "noticeContent") String noticeContent,
                                                     @NoticeType @NotBlank(message = "验证类型不能为空") @RequestParam(value = "noticeType") String noticeType) {
        if (Objects.equal(SysConst.NoticeType.PHONE.getType(), noticeType)) {
            userService.findByPhoneUnDelete(noticeContent);
        } else {
            throw new OperationException("暂时只支持短信验证码登录");
        }

        String ip = NetworkUtil.getLocalIp(request);
        verificationCodeService.sendCode(ip, noticeContent, noticeType, SysConst.VerificationCodeType.LOGIN.getType());
        return success("发送成功");
    }


//    @PostMapping(value = "validatePhoneByRegister")
//    public ResultMessage validatePhoneByRegister(@NotBlank(message = "手机号不能为空")
//                                                 @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                                 @RequestParam String phone) {
//        userService.validatePhoneByRegister(phone);
//        return success();
//
//    }
//
//
//    @PostMapping(value = "sendPhoneCodeByRegister")
//    @SaveLog(desc = "发送手机验证码执行注册操作")
//    public ResultMessage sendPhoneCodeByRegister(HttpServletRequest request,
//                                                 @NotBlank(message = "手机号不能为空")
//                                                 @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                                 @RequestParam String phone) {
//        String ip = NetworkUtil.getLocalIp(request);
//        userService.validatePhoneByRegister(phone);
//        verificationCodeService.sendCode(ip, phone, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.REGISTER.getType());
//        return success("发送成功");
//    }

//    @PostMapping(value = "register")
//    @SaveLog(desc = "注册")
//    public ResultMessage register(@NotBlank(message = "手机号不能为空")
//                                  @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                  @RequestParam String phone,
//                                  @NotBlank(message = "验证码不能为空")
//                                  @RequestParam String code,
//                                  @NotBlank(message = "密码不能为空")
//                                  @RequestParam String password) {
//        boolean state = verificationCodeService.validateCode(phone, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.REGISTER.getType());
//        if (state) {
//            userService.saveUserStudent(phone, password);
//            return success("保存成功");
//        } else {
//            return failure("验证失败");
//        }
//    }

//    @PostMapping(value = "validatePhoneCodeByForget")
//    public ResultMessage validatePhoneCodeByForget(@NotBlank(message = "手机号不能为空")
//                                                   @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                                   @RequestParam String phone) {
//        userService.validatePhoneByForget(phone);
//        return success();
//
//    }

//    @PostMapping(value = "sendPhoneCodeByForget")
//    @SaveLog(desc = "发送手机验证码执行重置密码操作")
//    public ResultMessage sendPhoneCodeByForget(HttpServletRequest request,
//                                               @NotBlank(message = "手机号不能为空")
//                                               @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                               @RequestParam String phone) {
//        userService.validatePhoneByForget(phone);
//        String ip = NetworkUtil.getLocalIp(request);
//        verificationCodeService.sendCode(ip, phone, SysConst.NoticeType.PHONE.getType(), SysConst.VerificationCodeType.FORGET.getType());
//        return success("发送成功");
//    }
//
//    @PostMapping(value = "modifyPassword")
//    @SaveLog(desc = "修改密码")
//    @DistributedLock
//    public ResultMessage modifyPassword(@NotBlank(message = "手机号不能为空")
//                                        @Pattern(regexp = "^1([3456789])\\d{9}$", message = "手机号码格式错误")
//                                        @RequestParam String phone,
//                                        @NotBlank(message = "验证码不能为空")
//                                        @RequestParam String code,
//                                        @NotBlank(message = "密码不能为空") @RequestParam String password) {
//        boolean state = verificationCodeService.validateCode(phone, SysConst.NoticeType.PHONE.getType(), code, SysConst.VerificationCodeType.FORGET.getType());
//        if (state) {
//            userService.modifyPassword(phone, password);
//            return success("保存成功");
//        } else {
//            return failure("验证失败");
//        }
//
//    }
}
