package com.example.datasource.base.domain;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/30 15:58
 * description: 分页类
 */
public class CustomPage extends BaseIdPageEntity {

    public CustomPage(int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
    }
}
