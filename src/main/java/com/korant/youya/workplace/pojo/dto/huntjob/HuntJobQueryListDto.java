package com.korant.youya.workplace.pojo.dto.huntjob;

import com.korant.youya.workplace.pojo.PageParam;
import lombok.Data;

/**
 * @ClassName HuntJobQueryListDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/16 11:34
 * @Version 1.0
 */
@Data
public class HuntJobQueryListDto extends PageParam {

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

    /**
     * 行业编码
     */
    private String industryCode;

    /**
     * 领域编码
     */
    private String sectorCode;

    /**
     * 职位编码
     */
    private String positionCode;
}
