package com.example.datasource.service;


import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.UserPermission;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface UserPermissionService extends BaseService<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);

}
