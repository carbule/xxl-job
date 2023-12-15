package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName EnterpriseBusinessLicenseVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/15 10:25
 * @Version 1.0
 */
@Data
public class EnterpriseBusinessLicenseVo {

    /**
     * 企业id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 社会信用代码
     */
    private String socialCreditCode;

    /**
     * 成立日期
     */
    private LocalDate establishDate;

    /**
     * 法人
     */
    private String corporation;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 营业执照
     */
    private String businessLicense;
}
