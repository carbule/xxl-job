package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.employstatus.EmployStatusModifyDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionQueryDto;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoByPositionCodeVo;
import com.korant.youya.workplace.service.ExpectedPositionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户意向职位表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@RestController
@RequestMapping("/expectedPosition")
public class ExpectedPositionController {

    @Resource
    private ExpectedPositionService expectedPositionService;

    /**
     * 根据职位查看求职者
     *
     * @param
     * @return
     */
    @PostMapping("/queryListByPositionCode")
    public R<?> queryListByPositionCode(@RequestBody @Valid ExpectedPositionQueryDto expectedPositionQueryDto) {
        Page<ExpectedPositionInfoByPositionCodeVo> page = expectedPositionService.queryListByPositionCode(expectedPositionQueryDto);
        return R.success(page);
    }

}
