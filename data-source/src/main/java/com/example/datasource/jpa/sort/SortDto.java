package com.example.datasource.jpa.sort;

import com.example.common.utils.SysConst;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
@Data
@AllArgsConstructor
public class SortDto {

    private String orderType;

    private String orderField;

    public SortDto(String orderField) {
        this.orderField = orderField;
        this.orderType = SysConst.SortOrder.DESC.getCode();
    }
}
