package com.example.common.dto.input;

import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/20 9:02
 * description:
 */
@Getter
@Setter
public class CommentReplyPageInputDTO extends PageInputDTO {
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

}
