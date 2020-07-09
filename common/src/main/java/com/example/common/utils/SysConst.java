package com.example.common.utils;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 10:18
 * description: 系统常量
 */
public class SysConst {

    public static final String ORIGINAL_IMAGE_FILE_URL = "/app/file/findOriginalImg?topicImgId=";
    public static final String COMPRESS_IMAGE_FILE_URL = "/app/file/findCompressImg?topicImgId=";

    public static final int DEFAULT_BATCH_SIZE = 200;
    public static final String DEFAULT_SORT_NAME = "createdTime";

    public static final String DEFAULT_USERNAME = "时光";
    public static final Long DEFAULT_INTEGRAL = 100L;
    public static final String DEFAULT_PASSWORD = "123456";


    ///////////////////////////////////////////////////////////////////////////
    // 通用常量 -- 多实体类通用
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设备信息
     */
    @Getter
    @AllArgsConstructor
    public enum DeviceInfo {
        /**
         * 手机
         */
        MOBILE("mobile", "手机"),
        /**
         * 网页
         */
        WEB("web", "网页");

        /**
         * type
         */
        private final String type;
        /**
         * name
         */
        private final String name;
    }


    @Getter
    @AllArgsConstructor
    public enum AppSystemType {
        /**
         * android
         */
        ANDROID("android"),
        /**
         * ios
         */
        IOS("ios");
        /**
         * type
         */
        private  final String type;
    }

    /**
     * 搜索范围
     */
    @Getter
    @AllArgsConstructor
    public enum SearchArea {
        /**
         *
         */
        ALL("all", "全部"),
        /**
         *
         */
        TITLE("title", "标题"),
        /**
         *
         */
        CONTENT("content", "内容");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }

    public static Optional<SearchArea> getSearchAreaByType(String type) {
        return Arrays.stream(SearchArea.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 排序
     */
    @Getter
    @AllArgsConstructor
    public enum SortOrder {
        /**
         *
         */
        ASC("asc"),
        /**
         *
         */
        DESC("desc");
        /**
         *
         */
        private final String code;
    }

    public static Optional<SortOrder> getSortOrderByType(String code) {
        return Arrays.stream(SortOrder.values())
                .filter(replyType -> StringUtils.equals(code, replyType.getCode()))
                .findFirst();
    }

    /**
     * 时间类型
     */
    @Getter
    @AllArgsConstructor
    public enum TimeType {
        /**
         *
         */
        CUSTOM_TIME("define", "自定义时间"),
        /**
         *
         */
        ALL("all", "全部"),
        /**
         *
         */
        TODAY("today", "今天"),
        /**
         *
         */
        YESTERDAY("yesterday", "昨天"),
        /**
         *
         */
        NEARLY_SEVEN_DAYS("nearlySevenDays", "近7天"),
        /**
         *
         */
        NEARLY_THIRTY_DAYS("nearlyThirtyDays", "近30天");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;

    }


    public static Optional<TimeType> getTimeTypeByType(String type) {
        return Arrays.stream(TimeType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 删除状态
     */
    @Getter
    @AllArgsConstructor
    public enum DeleteState {
        /**
         *
         */
        UN_DELETED((short) 0, "未删除"),
        /**
         *
         */
        DELETE((short) 1, "删除");

        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;
    }

    /**
     * 显示状态
     */
    @Getter
    @AllArgsConstructor
    public enum ShowState {

        /**
         *
         */
        HIDE((short) 0, "hide", "隐藏"),
        /**
         *
         */
        DISPLAY((short) 1, "display", "显示");

        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }

    /**
     * 开启状态
     */
    @Getter
    @AllArgsConstructor
    public enum EnabledState {

        /**
         *
         */
        OFF((short) 0, "停用"),
        /**
         *
         */
        ON((short) 1, "开启");
        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;

    }

    /**
     * 验证码类型
     */
    @Getter
    @AllArgsConstructor
    public enum VerificationCodeType {

        /**
         *
         */
        REGISTER("register", "账户注册"),
        /**
         *
         */
        LOGIN("login", "账户登陆"),
        /**
         *
         */
        FORGET("forget", "密码重置");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }

    /**
     * 通知类型
     */
    @Getter
    @AllArgsConstructor
    public enum NoticeType {

        /**
         *
         */
        EMAIL("email", "邮件"),
        /**
         *
         */
        PHONE("phone", "短信");
        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;

    }

    public static Optional<NoticeType> getNoticeTypeByType(String type) {
        return Arrays.stream(NoticeType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }
    ///////////////////////////////////////////////////////////////////////////
    // 业务常量
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 登录类型
     */
    @Getter
    @AllArgsConstructor
    public enum LoginType {

        /**
         *
         */
        AJAX("ajax", "ajax登陆"),
        /**
         *
         */
        TOKEN("token", "token登陆"),
        /**
         *
         */
        VERIFICATION_CODE("verificationCode", "验证码登陆");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;

    }


    /**
     * 账户状态
     */
    @Getter
    @AllArgsConstructor
    public enum AccountState {

        /**
         *
         */
        FROZEN((short) 0, "冻结"),
        /**
         *
         */
        NORMAL((short) 1, "正常");
        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;

    }

    /**
     * 账户类型
     */
    @Getter
    @AllArgsConstructor
    public enum AccountType {

        /**
         *
         */
        ADMIN("admin", "管理员用户"),
        /**
         *
         */
        STUDENT_PRESIDENT("studentPresident", "学生会用户"),
        /**
         *
         */
        STUDENT("student", "学生用户");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }

    public static Optional<AccountType> getAccountTypeByType(String type) {
        return Arrays.stream(AccountType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 账户类型
     */
    @Getter
    @AllArgsConstructor
    public enum PermissionType {

        /**
         *
         */
        ADMIN("admin", "管理员用户"),
        /**
         *
         */
        STUDENT_PRESIDENT("studentPresident", "学生会用户");

        private final String type;
        private final String name;
    }

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    public enum Sex {

        /**
         *
         */
        WOMEN((short) 0, "女"),
        /**
         *
         */
        MEN((short) 1, "男"),
        /**
         *
         */
        UNKNOWN((short) 99, "未知");

        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;

    }

    public static Optional<Sex> getSexByCode(Short code) {
        return Arrays.stream(Sex.values())
                .filter(replyType -> code.equals(replyType.getCode()))
                .findFirst();
    }


    /**
     * 状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /**
         *
         */
        IN_RELEASE("inRelease", "发布中"),
        /**
         *
         */
        NEW_RELEASE("newRelease", "新发布"),
        /**
         *
         */
        AFTER_RELEASE("afterRelease", "发布后"),
        /**
         *
         */
        SOLVE("solve", "已解决"),
        /**
         *
         */
        SELL_OUT("sellOut", "卖出"),
        /**
         *
         */
        LOWER_SHELF("lowerShelf", "下架");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum ReplyType {
        /**
         *
         */
        COMMENT((short) 1, "回复评论"),
        /**
         *
         */
        REPLY((short) 2, "回复别人的回复");

        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum TopicType {
        /**
         *
         */
        TOPIC_TYPE_1((short) 1, "交易"),
        /**
         *
         */
        TOPIC_TYPE_2((short) 2, "讯息"),
        /**
         *
         */
        TOPIC_TYPE_3((short) 3, "问答"),
        /**
         *
         */
        TOPIC_TYPE_4((short) 4, "题库"),
        /**
         *
         */
        TOPIC_TYPE_5((short) 5, "时光");

        /**
         *
         */
        private final Short code;
        /**
         *
         */
        private final String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum ZanType {
        /**
         *
         */
        ZAN_TOPIC((short) 1, "主题"),
        /**
         *
         */
        ZAN_COMMENT((short) 2, "评论");

        private final Short code;
        private final String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum CommentState {
        /**
         *
         */
        ADOPT("adopt", "采纳"),
        /**
         *
         */
        NOT_ADOPTED("notAdopted", "未采纳");

        /**
         *
         */
        private final String type;
        /**
         *
         */
        private final String name;
    }


}
