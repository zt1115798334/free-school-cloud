package com.example.shiro.service.impl;

import com.example.datasource.entity.VerificationCodeLog;
import com.example.datasource.repo.VerificationCodeLogRepository;
import com.example.shiro.service.VerificationCodeLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class VerificationCodeLogServiceImpl implements VerificationCodeLogService {

    private final VerificationCodeLogRepository verificationCodeLogRepository;

    @Override
    public VerificationCodeLog save(VerificationCodeLog verificationCodeLog) {
        return verificationCodeLogRepository.save(verificationCodeLog);
    }

    @Override
    public void deleteByTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        verificationCodeLogRepository.deleteByCreatedTimeBetween(startTime, endTime);
    }
}
