package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName HRVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/22 15:10
 * @Version 1.0
 */
@Data
public class HRVo {

    /**
     * 用户id
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
     * 用户手机号
     */
    private String phone;

    /**
     * 员工角色
     */
    private String roleName;
}