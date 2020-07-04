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
public class KnowingOutputDTO extends TopicOutputDTO {
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
     * 商品描述
     */
    private String describeContent;
    /**
     * 积分
     */
    private Long integral;

    public KnowingOutputDTO(UserOutputDTO user, String state, boolean userState, boolean zanState, Long zanNum, List<UserOutputDTO> zanUsers, boolean collectionState, Long commentNum, Long browsingVolume, List<ImagePathOutputDTO> topicImgList,
                            Long id, String title, String dateTime, String describeContent, Long integral) {
        super(user, state, userState, zanState, zanNum, zanUsers, collectionState, commentNum, browsingVolume, topicImgList);
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.describeContent = describeContent;
        this.integral = integral;
    }
}
