package com.korant.youya.workplace.pojo.vo.talentpool;

import lombok.Data;

/**
 * @ClassName TalentRecruitmentRecordsVo
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/8 14:31
 * @Version 1.0
 */
@Data
public class TalentRecruitmentRecordsVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 招聘流程实列id
     */
    private Long recruitProcessInstanceId;

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
     * 个性签名
     */
    private String personalSignature;

    /**
     * 领域名称
     */
    private String sectorName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 职业群等级
     */
    private String organizationLevel;

    /**
     * 职业等级
     */
    private String classLevel;

    /**
     * 最低薪资
     */
    private Integer minSalary;

    /**
     * 最高薪资
     */
    private Integer maxSalary;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 关联职位名称
     */
    private String associationPositionName;

    /**
     * 关联职位最低薪资
     */
    private Integer associationPositionMinSalary;

    /**
     * 关联职位最高薪资
     */
    private Integer associationPositionMaxSalary;

    /**
     * 流程环节
     */
    private Long processStep;
}
