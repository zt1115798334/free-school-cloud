package com.example.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/14 13:16
 * description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO implements Serializable {


    /**
     * 用户名Id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 个人签名
     */
    private String personalSignature;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private Short sex;
    /**
     * 积分
     */
    private Long integral;
    /**
     * 学校标识
     */
    private Short schoolCode;
    /**
     * 学校
     */
    private String school;
    /**
     * 账户类型：{admin :管理员用户,studentPresident:学生会用户,student:学生用户}
     * {@link SysConst.AccountType}
     */
    private String accountType;
    /**
     * 用户头像链接
     */
    private ImagePathOutputDTO userImgUrl;
}