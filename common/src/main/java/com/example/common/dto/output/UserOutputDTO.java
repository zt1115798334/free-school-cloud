package com.example.common.dto.output;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/14 13:16
 * description:
 */
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserOutputDTO implements Serializable {


    /**
     * 用户名Id
     */
    private Long id;
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
     * 账户类型：{admin :管理员用户,studentPresident:学生会用户,student:学生用户}
     */
    private String accountType;
    /**
     * 用户头像链接
     */
    private ImagePathOutputDTO userImgUrl;
}