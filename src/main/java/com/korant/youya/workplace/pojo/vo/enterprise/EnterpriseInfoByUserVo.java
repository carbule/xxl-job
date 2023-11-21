package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/20 13:52
 * @PackageName:com.korant.youya.workplace.pojo.vo.enterprise
 * @ClassName: EnterpriseInfoByUserVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class EnterpriseInfoByUserVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 认证状态
     */
    private Integer authStatus;

}
