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
public class CommentPageInputDTO extends PageInputDTO {
    /**
     * 主体id
     */
    private Long topicId;
}
