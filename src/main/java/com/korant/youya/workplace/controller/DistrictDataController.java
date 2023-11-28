package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataTreeVo;
import com.korant.youya.workplace.pojo.vo.district.QueryAllDataSortedByAcronymVo;
import com.korant.youya.workplace.service.DistrictDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/districtData")
public class DistrictDataController {

    @Resource
    private DistrictDataService districtDataService;

    /**
     * 查询所有地区数据
     *
     * @return
     */
    @GetMapping("/queryAllData")
    public R<?> queryAllData() {
        List<DistrictDataTreeVo> districtDataTreeVoList = districtDataService.queryAllData();
        return R.success(districtDataTreeVoList);
    }

    /**
     * 查询所有地区数据按缩略词排序
     *
     * @return
     */
    @GetMapping("/queryAllDataSortedByAcronym")
    public R<?> queryAllDataSortedByAcronym() {
        QueryAllDataSortedByAcronymVo acronymVo = districtDataService.queryAllDataSortedByAcronym();
        return R.success(acronymVo);
    }
}
