package com.korant.youya.workplace.pojo.vo.district;

import lombok.Data;

import java.util.List;

/**
 * @ClassName DistrictDataTreeDto
 * @Description
 * @Author chenyiqiang
 * @Date 2023/8/7 20:40
 * @Version 1.0
 */
@Data
public class DistrictDataTreeVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父主键
     */
    private Long pid;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 地区子节点
     */
    private List<DistrictDataTreeVo> children;
}
