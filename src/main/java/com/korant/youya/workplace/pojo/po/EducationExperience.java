package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 教育经历表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("education_experience")
public class EducationExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 学校名称
     */
    @TableField("school_name")
    private String schoolName;

    /**
     * 学历
     */
    @TableField("edu_level")
    private Integer eduLevel;

    /**
     * 开始时间
     */
    @TableField("start_time")
    @DateTimeFormat(pattern = "yyyy-MM")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    @DateTimeFormat(pattern = "yyyy-MM")
    private String endTime;

    /**
     * 专业名称
     */
    @TableField("speciality_name")
    private String specialityName;

    /**
     * 学位名称
     */
    @TableField("degree_name")
    private String degreeName;

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
