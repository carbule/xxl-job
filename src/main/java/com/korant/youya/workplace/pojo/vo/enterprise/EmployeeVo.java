package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName EmployeeVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 11:19
 * @Version 1.0
 */
@Data
public class EmployeeVo {

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
}
