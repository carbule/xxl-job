package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName EnterpriseCreateFailureDetailVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/15 16:05
 * @Version 1.0
 */
@Data
public class EnterpriseCreateFailureDetailVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 授权书
     */
    private String powerOfAttorney;

    /**
     * 营业执照
     */
    private String businessLicense;
}
