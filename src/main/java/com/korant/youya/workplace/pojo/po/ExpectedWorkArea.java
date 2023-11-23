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
     * 状态id
     */
    @TableField("status_id")
    private Long statusId;

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
     * 行政区编码
     */
    @TableField("district_code")
    private String districtCode;

    /**
     * 顺序
     */
    @TableField("sort")
    private Integer sort;

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
