package com.korant.youya.workplace.pojo.dto.wechatjssign;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName WechatJsSignDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/21 11:02
 * @Version 1.0
 */
@Data
public class WechatJsSignDto {

    /**
     * 签名的url
     */
    @NotBlank(message = "url不能为空")
    private String url;
}
