package com.example.common.dto.input;

import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:48
 * description:条件查询
 */
@Getter
@Setter
public class UserInputDTO extends PageInputDTO {

    private String account;

    //    @AccountType
    private String accountType;

    private String userName;
}
