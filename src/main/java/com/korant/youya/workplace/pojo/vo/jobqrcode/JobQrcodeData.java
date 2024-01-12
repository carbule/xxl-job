package com.korant.youya.workplace.pojo.vo.jobqrcode;

import lombok.Data;

/**
 * @ClassName JobQrcodeData
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/12 14:43
 * @Version 1.0
 */
@Data
public class JobQrcodeData {

    /**
     * 主键
     */
    private Long id;

    /**
     * 职位id
     */
    private Long jobId;

    /**
     * 推荐人id
     */
    private Long referee;

    /**
     * 是否分享收益 0-不分享 1-分享
     */
    private Integer isShare;
}
