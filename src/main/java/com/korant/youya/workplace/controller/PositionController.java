package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;
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
     * 查询所有行业
     *
     * @return
     */
    @GetMapping("/queryAllIndustries")
    public R<?> queryAllIndustries() {
        List<PositionDataVo> positionDataVoList = positionService.queryAllIndustries();
        return R.success(positionDataVoList);
    }

    /**
     * 根据行业code查询所有职位
     *
     * @return
     */
    @GetMapping("/queryPositionsByIndustryCode/{industryCode}")
    public R<?> queryPositionsByIndustryCode(@PathVariable("industryCode") String industryCode) {
        List<PositionDataTreeVo> positionDataTreeVoList = positionService.queryPositionsByIndustryCode(industryCode);
        return R.success(positionDataTreeVoList);
    }
}
