package com.example.datasource.entity;

import com.example.datasource.base.domain.BaseIdEntity;
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
 * date: 2019/07/15 17:20
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_permission")
public class Permission extends BaseIdEntity {
    /**
     * 权限字符串
     */
    private String permission;
    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型  admin 权限，studentPresident 学生权限
     */
    private String permissionType;

    /**
     * url地址
     */
    private String url;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


}
