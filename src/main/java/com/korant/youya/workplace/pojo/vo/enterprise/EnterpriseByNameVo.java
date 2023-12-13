package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName EnterpriseByNameVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/13 10:26
 * @Version 1.0
 */
@Data
public class EnterpriseByNameVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;
}
