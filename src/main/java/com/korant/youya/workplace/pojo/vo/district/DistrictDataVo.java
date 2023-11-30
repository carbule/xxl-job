package com.korant.youya.workplace.pojo.vo.district;

import lombok.Data;

/**
 * @ClassName DistrictDataVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/28 14:48
 * @Version 1.0
 */
@Data
public class DistrictDataVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 地区等级
     */
    private Integer level;

    /**
     * 缩略词
     */
    private String acronym;
}
