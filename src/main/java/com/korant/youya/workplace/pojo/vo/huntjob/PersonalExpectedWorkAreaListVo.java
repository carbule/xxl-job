package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName PersonalExpectedWorkAreaListVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/30 9:58
 * @Version 1.0
 */
@Data
public class PersonalExpectedWorkAreaListVo {

    /**
     * 主键
     */
    private Long id;

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
     * 国家名称
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
