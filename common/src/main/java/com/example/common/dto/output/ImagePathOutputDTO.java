package com.example.common.dto.output;

import com.example.common.utils.SysConst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/27 11:50
 * description:
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ImagePathOutputDTO {

    private String originalImageFileUrl;
    private String compressImageFileUrl;

    public ImagePathOutputDTO(Long topicImgId) {
        this.originalImageFileUrl = SysConst.ORIGINAL_IMAGE_FILE_URL + topicImgId;
        this.compressImageFileUrl = SysConst.COMPRESS_IMAGE_FILE_URL + topicImgId;
    }
}
