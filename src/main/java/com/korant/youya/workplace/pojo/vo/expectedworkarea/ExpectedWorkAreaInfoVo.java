package com.korant.youya.workplace.pojo.vo.expectedworkarea;

import lombok.Data;

/**
 * @Author duan-zhixiao
 * @Date 2023/11/16 14:59
 * @PackageName:com.korant.youya.workplace.pojo.dto.expectedworkarea
 * @ClassName: ExpectedWorkAreaInfoDto
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaInfoVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 状态id
     */
    private Long statusId;

    /**
     * 国家id
     */
    private Long countryId;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省份id
     */
    private Long provinceId;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市级id
     */
    private Long cityId;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 行政区id
     */
    private Long districtId;

    /**
     * 行政区名称
     */
    private String districtName;

}
