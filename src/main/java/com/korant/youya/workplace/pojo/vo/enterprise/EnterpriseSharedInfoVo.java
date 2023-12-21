package com.korant.youya.workplace.pojo.vo.enterprise;

import lombok.Data;

/**
 * @ClassName EnterpriseSharedInfoVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/21 16:09
 * @Version 1.0
 */
@Data
public class EnterpriseSharedInfoVo {

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
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 二维码地址
     */
    private String qrcodeUrl;
}
