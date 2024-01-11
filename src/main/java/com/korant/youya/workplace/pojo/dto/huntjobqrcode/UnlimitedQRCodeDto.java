package com.korant.youya.workplace.pojo.dto.huntjobqrcode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName UnlimitedQRCodeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/11 19:35
 * @Version 1.0
 */
@Data
public class UnlimitedQRCodeDto {

    /**
     * 页面
     */
    @NotBlank(message = "页面地址不能为空")
    private String page;

    /**
     * 求职id
     */
    @NotNull(message = "求职id不能为空")
    private Long huntId;

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
