package com.korant.youya.workplace.pojo.vo.user;

import lombok.Data;

/**
 * @ClassName UserPublicInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/16 14:07
 * @Version 1.0
 */
@Data
public class UserPublicInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 用户性别
     */
    private Integer gender;
}
