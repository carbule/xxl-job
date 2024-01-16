package com.korant.youya.workplace.pojo.vo.job;

import lombok.Data;

/**
 * @ClassName JobShareInfo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/12 11:41
 * @Version 1.0
 */
@Data
public class JobShareInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 推荐奖励
     */
    private Integer award;

    /**
     * 推荐人头像
     */
    private String refereeAvatar;

    /**
     * 推荐人姓氏
     */
    private String refereeLastName;

    /**
     * 推荐人名字
     */
    private String refereeFirstName;

    /**
     * 推荐人性别
     */
    private Integer refereeGender;
}
