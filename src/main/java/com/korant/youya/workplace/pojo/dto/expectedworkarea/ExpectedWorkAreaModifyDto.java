package com.korant.youya.workplace.pojo.dto.expectedworkarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/16 15:06
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedworkarea
 * @ClassName: ExpectedWorkAreaModifyDto
 * @Description:
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaModifyDto {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 国家编码
     */
    @NotNull(message = "国家编码不能为空")
    private String countryCode;

    /**
     * 省份编码
     */
    @NotNull(message = "省份编码不能为空")
    private String provinceCode;

    /**
     * 市级编码
     */
    @NotNull(message = "市级编码不能为空")
    private String cityCode;

    /**
     * 行政区编码
     */
    @NotNull(message = "行政区编码不能为空")
    private String districtCode;

}
