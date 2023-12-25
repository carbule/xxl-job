package com.korant.youya.workplace.pojo.dto.enterprise;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName QueryEnterpriseInfoByQrcodeIdDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/22 14:43
 * @Version 1.0
 */
@Data
public class QueryEnterpriseInfoByQrcodeIdDto {

    /**
     * 企业邀请二维码id
     */
    @NotNull(message = "二维码id不能为空")
    private Long qrcodeId;
}
