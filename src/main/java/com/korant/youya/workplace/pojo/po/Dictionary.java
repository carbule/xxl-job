package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("yy_dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典类别id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 字典名称
     */
    @TableField("dict_name")
    private String dictName;

    /**
     * 字典值
     */
    @TableField("dict_value")
    private Integer dictValue;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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
