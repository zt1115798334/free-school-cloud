package com.example.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TopicOutputDTO implements Serializable {

    /**
     * 用户信息
     */
    private UserOutputDTO user;
    /**
     * 商品状态
     */
    private String state;
    /**
     * 用户状态
     */
    private boolean userState;
    /**
     * 当前用户赞状态
     */
    private boolean zanState;

    /**
     * 赞数量
     */
    private Long zanNum;

    /**
     * 点赞人集合
     */
    private List<UserOutputDTO> zanUsers;

    /**
     * 收藏状态
     */
    private boolean collectionState;

    /**
     * 评论数量
     */
    private Long commentNum;

    /**
     * 浏览量
     */
    private Long browsingVolume;

    /**
     * 图片集合
     */
    private List<ImagePathOutputDTO> topicImgList;

    TopicOutputDTO(UserOutputDTO user, String state, boolean userState, boolean zanState, Long zanNum) {
        this.user = user;
        this.state = state;
        this.userState = userState;
        this.zanState = zanState;
        this.zanNum = zanNum;
    }
}

