package com.example.datasource.entity;

import com.example.datasource.base.domain.BaseIdPageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 11:34
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_knowing")
public class Knowing extends BaseIdPageEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 问题描述
     */
    private String describeContent;
    /**
     * 积分
     */
    private Long integral;
    /**
     * 浏览量
     */
    private Long browsingVolume;
    /**
     * 交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决, lowerShelf，下架}
     */
    private String state;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public Knowing(Long id, String title, String describeContent, Long integral) {
        this.id = id;
        this.title = title;
        this.describeContent = describeContent;
        this.integral = integral;
    }

    public Knowing(String sortName, String sortOrder, int pageNumber, int pageSize, LocalDateTime startDateTime, LocalDateTime endDateTime, String searchArea, String searchValue) {
        super(sortName, sortOrder, pageNumber, pageSize, startDateTime, endDateTime, searchArea, searchValue);
    }
}
