package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户期望工作区域表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expected_work_area")
public class ExpectedWorkArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 意向id
     */
    @TableField("intention_id")
    private Long intentionId;

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
    @TableField("detailed_address")
    private String detailedAddress;

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
