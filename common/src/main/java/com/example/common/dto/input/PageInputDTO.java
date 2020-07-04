package com.example.common.dto.input;

import com.example.common.utils.SysConst;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/28 10:50
 * description:分页查询
 */
@Getter
@Setter
public class PageInputDTO implements Serializable {
    /**
     * 页数
     */
//    @Min(value = 1, message = "页数最小为1页")
    protected int pageNumber = 1;
    /**
     * 每页显示数量
     */
//    @Range(min = 1, max = 10000, message = "每页显示数量最大为10000条")
    protected int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortName = SysConst.DEFAULT_SORT_NAME;
    /**
     * 排序类型
     */
//    @SortOrder
    private String sortOrder = SysConst.SortOrder.DESC.getCode();
}
