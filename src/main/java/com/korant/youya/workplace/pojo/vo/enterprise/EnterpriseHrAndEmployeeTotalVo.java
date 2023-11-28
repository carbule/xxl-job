package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

import java.util.List;

/**
 * @Date 2023/11/27 15:21
 * @ClassName: EnterpriseVo
 * @Version 1.0
 */
@Data
public class EnterpriseHrAndEmployeeTotalVo {

    /**
     * hr总数
     */
    private Integer hr;

    /**
     * 员工总数
     */
    private Integer employee;

    /**
     * hr头像
     */
    private List<String> hrAvatar;

    /**
     * 员工头像
     */
    private List<String> employeeAvatar;

}
