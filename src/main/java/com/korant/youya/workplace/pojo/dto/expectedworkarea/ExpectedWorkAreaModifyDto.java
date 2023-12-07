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
    private Long id;

    /**
     * 意向id
     */
    private Long statusId;

    /**
     * 国家编码
     */
    @NotBlank(message = "国家编码不能为空")
    private String countryCode;

    /**
     * 省份编码
     */
    @NotBlank(message = "省份编码不能为空")
    private String provinceCode;

    /**
     * 市级编码
     */
    @NotBlank(message = "市级编码不能为空")
    private String cityCode;

    /**
     * 行政区编码
     */
    @NotBlank(message = "行政区编码不能为空")
    private String districtCode;
}
