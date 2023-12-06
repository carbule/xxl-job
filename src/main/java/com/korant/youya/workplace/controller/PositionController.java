package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
}
