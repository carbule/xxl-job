package com.korant.youya.workplace.pojo.dto.expectedworkarea;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName ExpectedWorkAreaModifyDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/6 17:42
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaModifyDto {

    /**
     * 主键
     */
    private Long areaId;

    /**
     * 意向id
     */
    private Long statusId;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 省份编码
     */
    @NotBlank(message = "省份编码不能为空")
    private String provinceCode;

    /**
     * 市级编码
     */
    private String cityCode;
}
