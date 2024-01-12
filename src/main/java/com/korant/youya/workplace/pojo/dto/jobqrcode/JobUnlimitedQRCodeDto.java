package com.korant.youya.workplace.pojo.dto.jobqrcode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName JobUnlimitedQRCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/12 13:53
 * @Version 1.0
 */
@Data
public class JobUnlimitedQRCodeDto {

    /**
     * 页面
     */
    @NotBlank(message = "页面地址不能为空")
    private String page;

    /**
     * 求职id
     */
    @NotNull(message = "职位id不能为空")
    private Long jobId;

    /**
     * 二维码id
     */
    private Long qrId;

    /**
     * 是否分享收益 0-不分享 1-分享
     */
    @NotNull(message = "是否继续分享状态不能为空")
    private Integer isShare;
}
