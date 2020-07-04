package com.example.common.dto.input;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
public class StorageInformationInputDTO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String describeContent;
}
