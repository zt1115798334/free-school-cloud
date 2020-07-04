package com.example.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentStatusOutputDTO extends TopicOutputDTO {

    Long commentId;
    /**
     * 主体id
     */
    private Long topicId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createdTime;

    /**
     * 评论回复数量
     */
    private Long commentReplyNum;

    List<CommentReplyStatusOutputDTO> commentReplyStatusOutputDTOList;

    public CommentStatusOutputDTO(UserOutputDTO user, String state, boolean userState, boolean zanState, Long zanNum, Long commentId, Long topicId, String content, String createdTime, Long commentReplyNum) {
        super(user, state, userState, zanState, zanNum);
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
        this.createdTime = createdTime;
        this.commentReplyNum = commentReplyNum;
    }

    public CommentStatusOutputDTO(UserOutputDTO user, String state, boolean userState, boolean zanState, Long zanNum, Long commentId, Long topicId, String content, String createdTime, Long commentReplyNum, List<CommentReplyStatusOutputDTO> commentReplyStatusOutputDTOList) {
        super(user, state, userState, zanState, zanNum);
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
        this.createdTime = createdTime;
        this.commentReplyNum = commentReplyNum;
        this.commentReplyStatusOutputDTOList = commentReplyStatusOutputDTOList;
    }
}
