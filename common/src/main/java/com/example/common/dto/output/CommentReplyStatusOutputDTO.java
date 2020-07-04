package com.example.common.dto.output;

import lombok.*;

/**
 * @author zhang
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CommentReplyStatusOutputDTO {
    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 1为回复评论，2为回复别人的回复
     */
    private Short replyType;
    /**
     * 回复目标id，reply_type为1时，是comment_id，reply_type为2时为回复表的id
     */
    private Long replyId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复目标id
     */
    private Long toUserId;

    /**
     * 回复目标
     */
    private UserOutputDTO toUserName;
    /**
     * 回复用户id
     */
    private Long fromUserId;

    /**
     * 回复用户
     */
    private UserOutputDTO fromUser;

    /**
     * 创建时间
     */
    private String createdTime;
}
