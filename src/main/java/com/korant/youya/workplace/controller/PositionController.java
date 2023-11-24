package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.position.PositionData;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 职位信息表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Resource
    private PositionService positionService;

    /**
     * 查询所有职位数据
     *
     * @return
     */
    @GetMapping("/queryAllData")
    public R<?> queryAllData() {
        List<PositionDataTreeVo> positionDataTreeVoList = positionService.queryAllData();
        return R.success(positionDataTreeVoList);
    }

    /**
     * 查询所有行位
     *
     * @return
     */
    @GetMapping("/queryIndustry")
    public R<?> queryIndustry() {
        List<PositionData> positionDataList = positionService.queryIndustry();
        return R.success(positionDataList);
    }

//    /**
//     * 根据行位查询所有领域
//     *
//     * @return
//     */
//    @GetMapping("/querySector/{code}")
//    public R<?> querySector(@PathVariable("code") String code) {
//        List<PositionData> positionDataList = positionService.querySector(code);
//        return R.success(positionDataList);
//    }

    /**
     * 根据领域查询所有职位
     *
     * @return
     */
    @GetMapping("/queryPosition/{code}")
    public R<?> queryPosition(@PathVariable("code") String code) {
        List<PositionData> positionDataList = positionService.queryPosition(code);
        return R.success(positionDataList);
    }

}
