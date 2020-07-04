package com.example.datasource.service.impl;

import com.example.common.utils.DateUtils;
import com.example.datasource.entity.UserRegistration;
import com.example.datasource.repo.UserRegistrationRepository;
import com.example.datasource.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/09/25 11:36
 * description:
 */
@AllArgsConstructor
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;

//    private final StringRedisService stringRedisService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_UNCOMMITTED)
    public UserRegistration save(UserRegistration userRegistration) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Optional<UserRegistration> repository = userRegistrationRepository.findByUserIdAndRegistrationId(userRegistration.getUserId(), userRegistration.getRegistrationId());
        if (repository.isPresent()) {
            UserRegistration registration = repository.get();
            registration.setTime(currentDateTime);
            registration.setToken(userRegistration.getToken());
            userRegistration = userRegistrationRepository.save(registration);
        } else {
            userRegistration.setTime(currentDateTime);
            userRegistration = userRegistrationRepository.save(userRegistration);
        }
        return userRegistration;
    }

    @Override
    public List<String> findRegistrationIdByUserId(Long userId) {
        List<String> registrationIdList = userRegistrationRepository.queryRegistrationIdByUserId(userId);
        return registrationIdList;
    }
}
