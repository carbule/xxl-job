package com.korant.youya.workplace.pojo.vo.userblockedenterprise;

import lombok.Data;

/**
 * @ClassName QueryEnterpriseByNameVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/23 20:31
 * @Version 1.0
 */
@Data
public class QueryEnterpriseByNameVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;
}
