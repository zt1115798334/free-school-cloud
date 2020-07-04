package com.example.datasource.service.impl;


import com.example.datasource.entity.UserPermission;
import com.example.datasource.repo.UserPermissionRepository;
import com.example.datasource.service.UserPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
@AllArgsConstructor
@Service
public class UserPermissionServiceImpl implements UserPermissionService {


    private final UserPermissionRepository userPermissionRepository;

    @Override
    public Iterable<UserPermission> saveAll(Iterable<UserPermission> t) {
        return userPermissionRepository.saveAll(t);
    }

    @Override
    public List<UserPermission> findByUserId(Long userId) {
        return userPermissionRepository.findByUserId(userId);
    }
}
