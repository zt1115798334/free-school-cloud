package com.example.shiro.service.impl;

import com.example.common.utils.DateUtils;
import com.example.common.utils.SysConst;
import com.example.datasource.entity.Permission;
import com.example.datasource.entity.UserPermission;
import com.example.datasource.repo.PermissionRepository;
import com.example.shiro.service.PermissionService;
import com.example.shiro.service.UserPermissionService;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
@AllArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {


    private final PermissionRepository permissionRepository;

    private final UserPermissionService userPermissionService;

    @Override
    public List<Permission> findAll() {
        return (List<Permission>) permissionRepository.findAll();
    }

    @Override
    public List<Permission> findByIds(List<Long> id) {
        return (List<Permission>) permissionRepository.findAllById(id);
    }

    @Override
    public void saveSysSystemPermission(Long userId, String accountType) {
        List<Permission> permissionList = Lists.newLinkedList();
        if (Objects.equal(accountType, SysConst.AccountType.ADMIN.getType())) {
            permissionList = this.findAll();
        }
        if (Objects.equal(accountType, SysConst.AccountType.STUDENT_PRESIDENT.getType())) {
            permissionList = this.findStudentPresidentPermission();
        }
        List<UserPermission> userPermissionList = permissionList.stream()
                .map(permission -> new UserPermission(userId, permission.getId(), DateUtils.currentDateTime()))
                .collect(toList());
        userPermissionService.saveAll(userPermissionList);
    }

    @Override
    public List<Permission> findAdminPermission() {
        return this.findAll();
    }

    @Override
    public List<Permission> findStudentPresidentPermission() {
        return permissionRepository.findByPermissionType(SysConst.PermissionType.STUDENT_PRESIDENT.getType());
    }

    @Override
    public List<Permission> findByUserId(Long userId) {
        List<Long> permissionIdList = userPermissionService.findByUserId(userId).stream()
                .map(UserPermission::getPermissionId)
                .collect(toList());
        return this.findByIds(permissionIdList);
    }
}
