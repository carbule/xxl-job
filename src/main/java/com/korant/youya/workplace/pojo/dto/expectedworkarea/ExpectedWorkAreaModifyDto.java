package com.korant.youya.workplace.pojo.dto.expectedworkarea;

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
     * 国家编码
     */
    private String countryCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 市级编码
     */
    private String cityCode;

    /**
     * 行政区编码
     */
    private String districtCode;
}
