package com.korant.youya.workplace.pojo.vo.expectedworkarea;

import lombok.Data;

/**
 * @ClassName ExpectedWorkAreaVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/8 16:49
 * @Version 1.0
 */
@Data
public class ExpectedWorkAreaVo {

    /**
     * 期望区域id
     */
    private Long areaId;

    /**
     * 国家名称
     */
    private String workAreaCountryName;

    /**
     * 省份名称
     */
    private String workAreaProvinceName;

    /**
     * 市级名称
     */
    private String workAreaCityName;
}
