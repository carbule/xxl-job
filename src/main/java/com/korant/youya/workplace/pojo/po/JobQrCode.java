package com.korant.youya.workplace.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 职位推荐二维码表
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job_qr_code")
public class JobQrCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 二维码id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 职位id
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 推荐人id
     */
    @TableField("referee")
    private Long referee;

    /**
     * 是否分享收益 0-不分享 1-分享
     */
    @TableField("is_share")
    private Integer isShare;

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
     * 是否分享收益 0-不分享 1-分享
     */
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
