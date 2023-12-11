package com.korant.youya.workplace.pojo.vo.position;

import lombok.Data;

/**
 * @ClassName PositionDataTreeVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/8 13:53
 * @Version 1.0
 */
@Data
public class PositionDataVo {

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
}
