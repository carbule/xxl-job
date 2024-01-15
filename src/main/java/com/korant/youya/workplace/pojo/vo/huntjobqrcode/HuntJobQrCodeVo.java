package com.korant.youya.workplace.pojo.vo.huntjobqrcode;

import com.korant.youya.workplace.annotations.Dict;
import lombok.Data;

/**
 * @ClassName HuntJobQrCodeVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/15 11:27
 * @Version 1.0
 */
@Data
public class HuntJobQrCodeVo {

    /**
     * 主键
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
     * 用户名
     */
    private String firstName;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 职位
     */
    private String positionName;

    /**
     * 学历
     */
    @Dict(categoryCode = "education")
    private Integer eduLevel;

    /**
     * 工作年限
     */
    private Integer workExperience;

    /**
     * 期望最低工资
     */
    private Integer minExpectedSalary;

    /**
     * 期望最大工资
     */
    private Integer maxExpectedSalary;

    /**
     * 奖励金额
     */
    private Integer award;
}
