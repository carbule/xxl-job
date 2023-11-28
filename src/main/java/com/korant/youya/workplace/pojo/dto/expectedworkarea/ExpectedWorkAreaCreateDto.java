package com.korant.youya.workplace.pojo.dto.expectedworkarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Date 2023/11/16 15:03
 * @ClassName: ExpectedWorkAreaIncreaseDto
 * @Description:
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaCreateDto {

    /**
     * 国家编码
     */
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
    private String districtCode;

}
