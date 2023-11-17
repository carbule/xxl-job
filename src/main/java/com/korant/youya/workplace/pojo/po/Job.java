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
    private Integer id;

    /**
     * 企业id
     */
    @TableField("enterprise_id")
    private Integer enterpriseId;

    /**
     * 发布人id
     */
    @TableField("uid")
    private Integer uid;

    /**
     * 行业id
     */
    @TableField("industry_id")
    private Long industryId;

    /**
     * 领域id
     */
    @TableField("sector_id")
    private Long sectorId;

    /**
     * 职位id
     */
    @TableField("position_id")
    private Long positionId;

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
     * 国家id
     */
    @TableField("country_id")
    private Long countryId;

    /**
     * 省份id
     */
    @TableField("province_id")
    private Long provinceId;

    /**
     * 市级id
     */
    @TableField("city_id")
    private Long cityId;

    /**
     * 行政区id
     */
    @TableField("district_id")
    private Long districtId;

    /**
     * 详细地址
     */
    @TableField("detail_address")
    private String detailAddress;

    /**
     * 经度
     */
    @TableField("longitude")
    private String longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private String latitude;

    /**
     * 推荐奖励
     */
    @TableField("award")
    private Integer award;

    /**
     * 招聘人数
     */
    @TableField("recruitment_numbers")
    private Integer recruitmentNumbers;

    /**
     * 入职一人奖励金额
     */
    @TableField("personal_reward")
    private Integer personalReward;

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
