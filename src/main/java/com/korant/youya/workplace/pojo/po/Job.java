package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业职位表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 发布人id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 行业编码
     */
    @TableField("industry_code")
    private String industryCode;

    /**
     * 领域编码
     */
    @TableField("sector_code")
    private String sectorCode;

    /**
     * 职位编码
     */
    @TableField("position_code")
    private String positionCode;

    /**
     * 学历要求
     */
    @TableField("edu_requirements")
    private Integer eduRequirements;

    /**
     * 工作经验
     */
    @TableField("work_experience")
    private Integer workExperience;

    /**
     * 最低薪资
     */
    @TableField("min_salary")
    private Integer minSalary;

    /**
     * 最高薪资
     */
    @TableField("max_salary")
    private Integer maxSalary;

    /**
     * 职位描述
     */
    @TableField("job_desc")
    private String jobDesc;

    /**
     * 其他说明项
     */
    @TableField("other_info")
    private String otherInfo;

    /**
     * 国家编码
     */
    @TableField("country_code")
    private String countryCode;

    /**
     * 省份编码
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 市级编码
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 详细地址
     */
    @TableField("detail_address")
    private String detailAddress;

    /**
     * 经度
     */
    @TableField("longitude")
    private Float longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private Float latitude;

    /**
     * 推荐奖励
     */
    @TableField("award")
    private Integer award;

    /**
     * 面试奖励分配比例
     */
    @TableField("interview_reward_rate")
    private Integer interviewRewardRate;

    /**
     * 入职奖励分配比例
     */
    @TableField("onboard_reward_rate")
    private Integer onboardRewardRate;

    /**
     * 转正奖励分配比例
     */
    @TableField("full_member_reward_rate")
    private Integer fullMemberRewardRate;

    /**
     * 职位状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核状态
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
