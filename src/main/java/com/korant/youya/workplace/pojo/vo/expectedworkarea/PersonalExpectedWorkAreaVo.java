package com.korant.youya.workplace.pojo.vo.expectedworkarea;

import lombok.Data;

/**
 * @ClassName PersonalExpectedWorkAreaVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/7 14:56
 * @Version 1.0
 */
@Data
public class PersonalExpectedWorkAreaVo {

    /**
     * 期望区域id
     */
    private Long areaId;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 行政区名称
     */
    private String districtName;

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
