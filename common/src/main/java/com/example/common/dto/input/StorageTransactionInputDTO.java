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
public class StorageTransactionInputDTO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 价格
     */
    private Double price;
    /**
     * 商品描述
     */
    private String describeContent;
    /**
     * 联系方式
     */
    private String contactMode;

    /**
     * 联系人
     */
    private String contactPeople;

    /**
     * 地址
     */
    private String address;
}
