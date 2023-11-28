package com.korant.youya.workplace.pojo.vo.position;

import lombok.Data;

import java.util.List;

/**
 * @Date 2023/11/24 16:19
 * @ClassName: PositionData
 * @Version 1.0
 */
@Data
public class PositionData {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父主键
     */
    private Long pid;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位编码
     */
    private String code;

    private List<PositionData> positionDataList;

}
