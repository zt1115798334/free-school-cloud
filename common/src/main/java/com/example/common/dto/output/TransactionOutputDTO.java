package com.example.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutputDTO extends TopicOutputDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 时间
     */
    private String dateTime;

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

    public TransactionOutputDTO(UserOutputDTO user, String state, boolean userState, boolean zanState, Long zanNum, List<UserOutputDTO> zanUsers, boolean collectionState, Long commentNum, Long browsingVolume, List<ImagePathOutputDTO> topicImgList,
                                Long id, String title, String dateTime, Double price, String describeContent, String contactMode, String contactPeople, String address) {
        super(user, state, userState, zanState, zanNum, zanUsers, collectionState, commentNum, browsingVolume, topicImgList);
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.price = price;
        this.describeContent = describeContent;
        this.contactMode = contactMode;
        this.contactPeople = contactPeople;
        this.address = address;
    }
}
