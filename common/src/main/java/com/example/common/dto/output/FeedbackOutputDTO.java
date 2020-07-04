package com.example.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FeedbackOutputDTO {
    /**
     * id
     */
    private Long id;

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

    /**
     * 图片集合
     */
    private List<ImagePathOutputDTO> topicImgList;

    public FeedbackOutputDTO(Short feedbackType, String content, String contactMode) {
        this.feedbackType = feedbackType;
        this.content = content;
        this.contactMode = contactMode;
    }
}
