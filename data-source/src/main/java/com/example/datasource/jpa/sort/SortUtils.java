package com.example.datasource.jpa.sort;

import com.example.common.utils.SysConst;
import org.springframework.data.domain.Sort;

import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
public class SortUtils {

    public static Sort basicSort() {
        return basicSort(SysConst.SortOrder.DESC.getCode(), "id");
    }

    public static Sort basicSort(String orderType, String orderField) {
        return  Sort.by(Sort.Direction.fromString(orderType),orderField);
    }

    public static Sort basicSort(SortDto... sortDtoList) {
        Sort result = null;
        for (SortDto dto : sortDtoList) {
            if (result == null) {
                result = Sort.by(Sort.Direction.fromString(dto.getOrderType()),dto.getOrderField());
            } else {
                result = result.and(Sort.by(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
