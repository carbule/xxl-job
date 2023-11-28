package com.korant.youya.workplace.pojo.vo.enterprisetodo;

import lombok.Data;

/**
 * @Date 2023/11/22 14:58
 * @ClassName: EnterpriseEmployeeListVo
 * @Description:
 * @Version 1.0
 */
@Data
public class EnterpriseEmployeeListVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 工号
     */
    private String employeeId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户身份证号
     */
    private String identityCard;

}
