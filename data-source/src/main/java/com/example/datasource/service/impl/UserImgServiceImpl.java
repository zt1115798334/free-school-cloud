package com.example.datasource.service.impl;

import com.example.datasource.entity.UserImg;
import com.example.datasource.repo.UserImgRepository;
import com.example.datasource.service.UserImgService;
import com.example.datasource.utils.UserUtils;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/24 10:16
 * description:
 */
@AllArgsConstructor
@Service
public class UserImgServiceImpl implements UserImgService {

    private final UserImgRepository userImgRepository;

//    private final FileInfoService fileInfoService;


    @Override
    public UserImg save(UserImg userImg) {
        userImg.setState(ON);
        userImg.setDeleteState(UN_DELETED);
        userImg.setCreatedTime(currentDateTime());
        return userImgRepository.save(userImg);
    }

    @Override
    public UserImg findUserImgUrlByOn(Long userId) {
        return userImgRepository.findByUserIdAndStateAndDeleteState(userId, ON, UN_DELETED).orElse(UserUtils.getDefaultUserImg());
    }

    @Override
    public Map<Long, UserImg> findUserImgUrlByOn(List<Long> userIdList) {
        List<UserImg> userImgList = userImgRepository.findByUserIdInAndStateAndDeleteState(userIdList, ON, UN_DELETED);
        return userImgList.stream().collect(toMap(UserImg::getUserId, Function.identity()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void modifyUserImg(Long userId, Long imgId) {
        List<UserImg> userImgList = userImgRepository.findByUserId(userId);
        List<UserImg> needSave = userImgList.stream().peek(userImg -> {
            if (Objects.equal(imgId, userImg.getImgId())) {
                userImg.setState(ON);
            } else {
                userImg.setState(OFF);
            }
        }).collect(toList());
        userImgRepository.saveAll(needSave);
    }

}
