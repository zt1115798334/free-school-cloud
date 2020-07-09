package com.example.shiro.service;

import com.example.common.dto.input.UserInputDTO;
import com.example.common.dto.output.UserOutputDTO;
import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.User;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:27
 * description:
 */
public interface UserService extends BaseService<User, Long> {

    void updateLastLoginTime(Long userId);

    void saveUser(String phone, String password, String accountType);

    UserOutputDTO saveUser(User user);

    void modifyPassword(String phone, String password);

    void deleteUser(Long userId);

    void normalUser(Long userId);

    void increaseIntegral(Long sellerUserId, Long integral);

    void reduceIntegral(Long buyerUserId, Long integral);

    Optional<User> findOptByUserId(Long userId);

    Optional<User> findOptByAccount(String account);

    Optional<User> findOptByPhone(String phone);

    User findByPhoneUnDelete(String phone);

    User findByUserId(Long userId);

    UserOutputDTO findUserDtoByUserId(Long userId);

    List<UserOutputDTO> findUserDtoByUserId(List<Long> userIdList);

    Map<Long, UserOutputDTO> findMapUserDtoByUserId(List<Long> userIdList);

    PageImpl<UserOutputDTO> findUserDto(UserInputDTO voUser);

    void validatePhoneByRegister(String phone);

    void validatePhoneByForget(String phone);

}
