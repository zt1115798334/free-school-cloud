package com.example.shiro.service;

import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.UserImg;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/24 10:16
 * description:
 */
public interface UserImgService extends BaseService<UserImg, Long> {

    UserImg findUserImgUrlByOn(Long userId);

    Map<Long, UserImg> findUserImgUrlByOn(List<Long> userIdList);

    void modifyUserImg(Long userId, Long imgId);

}
