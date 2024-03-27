package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.position.PositionClassLevelVO;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
     * 根据父级编码查询子集
     */
    @GetMapping("/listPositionsByParent/{parentCode}")
    public R<List<PositionDataVo>> listPositionsByParent(@PathVariable String parentCode,
                                                         @RequestParam(required = false) Integer level) {
        return R.success(
                positionService.listPositionsByParent(parentCode, level)
        );
    }

    /**
     * 查询专业领域下的职业等级
     */
    @GetMapping("/listClassLevels/{sectorCode}")
    public R<List<PositionClassLevelVO>> listClassLevels(@PathVariable String sectorCode) {
        return R.success(
                positionService.listClassLevels(sectorCode)
        );
    }
}
