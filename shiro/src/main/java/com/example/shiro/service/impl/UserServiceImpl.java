package com.example.shiro.service.impl;

import com.example.common.dto.input.UserInputDTO;
import com.example.common.dto.output.UserOutputDTO;
import com.example.common.exception.custom.OperationException;
import com.example.common.utils.DateUtils;
import com.example.common.utils.SysConst;
import com.example.datasource.dto.DtoUtils;
import com.example.datasource.entity.User;
import com.example.datasource.entity.UserImg;
import com.example.datasource.jpa.PageUtils;
import com.example.datasource.jpa.SearchFilter;
import com.example.datasource.repo.UserRepository;
import com.example.datasource.utils.UserUtils;
import com.example.shiro.service.PermissionService;
import com.example.shiro.service.UserImgService;
import com.example.shiro.service.UserService;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:28
 * description:
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserImgService userImgService;

    private final PermissionService permissionService;


    //冻结
    private static final Short FROZEN = SysConst.AccountState.FROZEN.getCode();
    private static final Short NORMAL = SysConst.AccountState.NORMAL.getCode();
    private static final String ADMIN = SysConst.AccountType.ADMIN.getType();

    @Override
    public User save(User user) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Long userId = user.getId();
        if (userId != null) {
            Optional<User> pompUserOpt = userRepository.findById(userId);
            if (pompUserOpt.isPresent()) {
                User userO = pompUserOpt.get();
                userO.setAccount(user.getAccount());
                userO.setPassword(user.getPassword());
                userO.setUserName(user.getUserName());
                userO.setSalt(user.getSalt());
                userO.setPhone(user.getPhone());
                userO.setPersonalSignature(user.getPersonalSignature());
                userO.setAccountState(user.getAccountState());
                userO.setUpdatedTime(currentDateTime);
                userO.setDeleteState(UN_DELETED);
                user = userRepository.save(userO);
            }
        } else {
            user.setUserName(UserUtils.getDefaultUserName());
            user.setCreatedTime(currentDateTime);
            user.setUpdatedTime(currentDateTime);
            user.setDeleteState(UN_DELETED);
            user = userRepository.save(user);
            permissionService.saveSysSystemPermission(user.getId(), user.getAccountType());
        }

        return user;
    }

    @Override
    public void deleteById(Long aLong) {
        Optional<User> userOptional = userRepository.findById(aLong);
        userOptional.ifPresent(user -> {
            user.setDeleteState(DELETED);
            userRepository.save(user);
        });
    }

    @Override
    public Optional<User> findByIdNotDelete(Long id) {
        return userRepository.findByIdAndDeleteState(id, UN_DELETED);
    }

    @Override
    public List<User> findByIdsNotDelete(List<Long> id) {
        return userRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<User> findPageByEntity(User user) {
        List<SearchFilter> filters = buildQueryMap(user);
        Specification<User> specification = SearchFilter.bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(
                user.getPageNumber(),
                user.getPageSize());
        return userRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateLastLoginTime(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setLastLoginTime(DateUtils.currentDateTime());
            userRepository.save(user);
        });
    }

    @Override
    public void saveUser(String phone, String password, String accountType) {
        this.validatePhoneByRegister(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        User user = new User(phone, encryptPassword, salt, UserUtils.getDefaultUserName(), phone, SysConst.Sex
                .UNKNOWN.getCode(), 10L, SysConst.AccountState.NORMAL.getCode(), accountType);
        this.save(user);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserOutputDTO saveUser(User user) {
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        Long userId = user.getId();
        String userName = user.getUserName();
        Optional<User> optionalUser = userRepository.findByUserNameAndDeleteState(userName, UN_DELETED);
        if (optionalUser.isPresent()) {
            if (!Objects.equal(optionalUser.get().getId(), userId)) {
                throw new OperationException("昵称重复");
            }
        }
        Optional<User> userOptional = userRepository.findByIdAndDeleteState(userId, UN_DELETED);
        User userBase = userOptional.orElseThrow(() -> new OperationException("用户已被删除"));
        userBase.setUserName(userName);
        userBase.setPhone(user.getPhone());
        userBase.setSex(user.getSex());
        userBase.setEmail(user.getEmail());
        userBase.setPersonalSignature(user.getPersonalSignature());
        userBase.setUpdatedTime(currentDateTime);
        userBase.setDeleteState(UN_DELETED);
        user = userRepository.save(userBase);
        UserImg userImg = userImgService.findUserImgUrlByOn(user.getId());
//        return RoChangeEntityUtils.resultRoUser(user, userImg);
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void modifyPassword(String phone, String password) {
        this.validatePhoneByForget(phone);
        User user = this.findByPhoneUnDelete(phone);
        String salt = UserUtils.getSalt();
        String encryptPassword = UserUtils.getEncryptPassword(phone, password, salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        this.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        this.deleteById(userId);
    }

    @Override
    public void normalUser(Long userId) {
        User user = this.findByUserId(userId);
        Short accountState = user.getAccountState();
        accountState = Objects.equal(accountState, FROZEN) ? NORMAL : FROZEN;
        user.setAccountState(accountState);
        userRepository.save(user);
    }

    @Override
    public void increaseIntegral(Long sellerUserId, Long integral) {
        User sellerUser = this.findByUserId(sellerUserId);
        sellerUser.setIntegral(sellerUser.getIntegral() + integral);
        userRepository.save(sellerUser);
    }

    @Override
    public void reduceIntegral(Long buyerUserId, Long integral) {
        User buyerUser = this.findByUserId(buyerUserId);
        buyerUser.setIntegral(buyerUser.getIntegral() - integral);
        userRepository.save(buyerUser);
    }

    @Override
    public Optional<User> findOptByUserId(Long userId) {
        return this.findByIdNotDelete(userId);
    }

    @Override
    public Optional<User> findOptByAccount(String account) {
        return userRepository.findByAccountAndDeleteState(account, UN_DELETED);
    }

    @Override
    public Optional<User> findOptByPhone(String phone) {
        return userRepository.findByPhoneAndDeleteState(phone, UN_DELETED);
    }

    @Override
    public User findByPhoneUnDelete(String phone) {
        Optional<User> byPhoneAndDeleteState = userRepository.findByPhoneAndDeleteState(phone, UN_DELETED);
        return byPhoneAndDeleteState.orElseThrow(() -> new OperationException("该用户不存在"));
    }


    @Override
    public User findByUserId(Long userId) {
        return this.findByIdNotDelete(userId).orElseThrow(() -> new OperationException("该用户不存在"));
    }

    @Override
    public UserOutputDTO findUserDtoByUserId(Long userId) {
        User user = this.findByIdNotDelete(userId).orElse(UserUtils.getDefaultUser());
        UserImg userImg = userImgService.findUserImgUrlByOn(userId);
        return DtoUtils.convertToUserOutputDTO(user, userImg);
    }

    @Override
    public List<UserOutputDTO> findUserDtoByUserId(List<Long> userIdList) {
        List<User> userList = findByIdsNotDelete(userIdList);
        Map<Long, UserImg> userImgMap = userImgService.findUserImgUrlByOn(userIdList);
        return DtoUtils.convertToUserOutputDTO(userList, userImgMap);
    }

    @Override
    public Map<Long, UserOutputDTO> findMapUserDtoByUserId(List<Long> userIdList) {
        return this.findUserDtoByUserId(userIdList).stream().collect(toMap(UserOutputDTO::getId, Function.identity()));
    }

    @Override
    public PageImpl<UserOutputDTO> findUserDto(UserInputDTO voUser) {
        User user = new User();
        user.setAccount(voUser.getAccount());
        user.setAccountType(voUser.getAccountType());
        user.setUserName(voUser.getUserName());
        Page<User> page = this.findPageByEntity(user);
        List<Long> userIdList = page.stream().map(User::getId).collect(toList());
        Map<Long, UserImg> userImgMap = userImgService.findUserImgUrlByOn(userIdList);
        List<UserOutputDTO> roUserList = Lists.newLinkedList();
        return new PageImpl<>(roUserList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public void validatePhoneByRegister(String phone) {
        Optional<User> userPhoneOptional = userRepository.findByPhoneAndDeleteState(phone, UN_DELETED);
        Optional<User> userAccountOptional = userRepository.findByAccountAndDeleteState(phone, UN_DELETED);
        if (userPhoneOptional.isPresent() || userAccountOptional.isPresent()) {
            throw new OperationException("该手机号已经注册，请直接登录");
        }
    }

    @Override
    public void validatePhoneByForget(String phone) {
        Optional<User> userPhoneOptional = userRepository.findByPhoneAndDeleteState(phone, UN_DELETED);
        Optional<User> userAccountOptional = userRepository.findByAccountAndDeleteState(phone, UN_DELETED);
        if (!userPhoneOptional.isPresent() || !userAccountOptional.isPresent()) {
            throw new OperationException("该手机号没有注册，请直接注册");
        }
    }

    private List<SearchFilter> buildQueryMap(User user) {
        List<SearchFilter> filters = Lists.newArrayList();
        if (StringUtils.isNotEmpty(user.getAccount())) {
            filters.add(new SearchFilter("account", user.getAccount(), SearchFilter.Operator.LIKE));
        }
        if (StringUtils.isNotEmpty(user.getUserName())) {
            filters.add(new SearchFilter("userName", user.getUserName(), SearchFilter.Operator.LIKE));
        }
        if (StringUtils.isNotEmpty(user.getAccountType())) {
            filters.add(new SearchFilter("accountType", user.getAccountType(), SearchFilter.Operator.EQ));
        }
        filters.add(new SearchFilter("deleteState", UN_DELETED, SearchFilter.Operator.EQ));
        return filters;
    }
}
