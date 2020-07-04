package com.example.common.dto.input;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@Getter
@Setter
public class StorageFeedbackInputDTO implements Serializable {

    /**
     *
     */
    private Short feedbackType;
    /**
     *
     */
    private String content;
    /**
     * 联系方式
     */
    private String contactMode;
}
