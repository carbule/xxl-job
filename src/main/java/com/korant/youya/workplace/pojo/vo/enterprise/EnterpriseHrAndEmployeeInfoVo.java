package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @Date 2023/11/28 10:49
 * @ClassName: EnterpriseHrAndEmployeeInfoVo
 * @Version 1.0
 */
@Data
public class EnterpriseHrAndEmployeeInfoVo {

    /**
     * userId
     */
    private Long id;

    /**
     * 头像
     */
    private String  avatar;

    /**
     * 角色id
     */
    private Long  rid;
}
