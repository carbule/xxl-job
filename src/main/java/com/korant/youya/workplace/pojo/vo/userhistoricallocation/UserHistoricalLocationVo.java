package com.korant.youya.workplace.pojo.vo.userhistoricallocation;

import lombok.Data;

/**
 * @ClassName UserHistoricalLocationVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/12/26 15:36
 * @Version 1.0
 */
@Data
public class UserHistoricalLocationVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;
}
