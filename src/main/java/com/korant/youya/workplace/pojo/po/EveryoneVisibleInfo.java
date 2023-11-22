package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 所有人可见信息表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("everyone_visible_Info")
public class EveryoneVisibleInfo implements Serializable {

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
     * 用户手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户微信号
     */
    @TableField("wechat_id")
    private String wechatId;

    /**
     * 用户QQ号
     */
    @TableField("qq")
    private String qq;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

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
     * 详细地址
     */
    @TableField("address")
    private String address;

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
