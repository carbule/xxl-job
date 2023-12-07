package com.korant.youya.workplace.pojo.vo.huntjob;

import lombok.Data;

/**
 * @ClassName HuntJobPersonalVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/28 16:49
 * @Version 1.0
 */
@Data
public class HuntJobPersonalVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 职位名称
     */
    private String positionName;

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
     * 期望最低工资
     */
    private Integer minExpectedSalary;

    /**
     * 期望最大工资
     */
    private Integer maxExpectedSalary;

    /**
     * 奖励金额
     */
    private Integer award;

    /**
     * 求职状态
     */
    private Integer status;
}
