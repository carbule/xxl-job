package com.korant.youya.workplace.pojo.vo.huntjobqrcode;

import lombok.Data;

/**
 * @ClassName HuntJobQrcodeData
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/11 17:31
 * @Version 1.0
 */
@Data
public class HuntJobQrcodeData {

    /**
     * 主键
     */
    private Long id;

    /**
     * 求职id
     */
    private Long huntId;

    /**
     * 推荐人id
     */
    private Long referee;

    /**
     * 是否分享收益 0-不分享 1-分享
     */
    private Integer isShare;
}
