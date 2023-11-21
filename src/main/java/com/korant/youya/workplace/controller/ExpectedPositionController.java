package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.service.ExpectedPositionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 查询用户求职意向职位
     *
     * @return
     */
    @GetMapping("/queryList")
    public R<?> queryList() {
        List<ExpectedPositionInfoVo> expectedPositionInfoVoList = expectedPositionService.findExpectedPositionInfo();
        return R.success(expectedPositionInfoVoList);
    }

    /**
     * 添加求职意向职位
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid ExpectedPositionCreateDto expectedPositionCreateDto) {
        expectedPositionService.create(expectedPositionCreateDto);
        return R.ok();
    }

    /**
     * 编辑求职意向职位
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid ExpectedPositionModifyDto expectedPositionModifyDto) {
        expectedPositionService.modify(expectedPositionModifyDto);
        return R.ok();
    }

    /**
     * 删除求职意向职位
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        expectedPositionService.delete(id);
        return R.ok();
    }

}
