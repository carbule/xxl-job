package com.korant.youya.workplace.pojo.vo.district;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QueryAllDataSortedByAcronymVo
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/28 14:45
 * @Version 1.0
 */
@Data
public class QueryAllDataSortedByAcronymVo {

    /**
     * 热门地区数据
     */
    private List<DistrictDataVo> popularRegions = new ArrayList<>();

    /**
     * 国内所有地区数据按缩略词排序
     */
    private List<List<DistrictDataVo>> districtDataList;
}
