package com.korant.youya.workplace.pojo.vo.district;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.pojo.vo.userhistoricallocation.UserHistoricalLocationVo;
import lombok.Data;

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
     * 历史访问城市
     */
    private List<UserHistoricalLocationVo> historicalLocationList;

    /**
     * 热门地区数据
     */
    private List<JSONObject> popularRegions;

    /**
     * 国内所有地区数据按缩略词排序
     */
    private List<List<DistrictDataVo>> districtDataList;
}
