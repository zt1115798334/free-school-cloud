package com.example.common.dto.input;

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
public class StorageUserVoStorageUser implements Serializable {

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
//    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 性别
     */
//    @Sex
    private Short sex;
    /**
     * 学校
     */
    private Short schoolCode;
}