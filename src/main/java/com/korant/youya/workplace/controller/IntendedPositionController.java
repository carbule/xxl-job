package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionModifyDto;
import com.korant.youya.workplace.pojo.vo.intendedposition.IntendedPositionInfoVo;
import com.korant.youya.workplace.service.IntendedPositionService;
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
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/intendedPosition")
public class IntendedPositionController {

    @Resource
    private IntendedPositionService intendedPositionService;

    /**
     * 查询用户求职意向
     *
     * @return
     */
    @GetMapping("/queryList")
    public R<?> queryList() {
        List<IntendedPositionInfoVo> intendedPositionInfoDtoList = intendedPositionService.findIntendedPositionInfo();
        return R.success(intendedPositionInfoDtoList);
    }

    /**
     * 添加求职意向
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid IntendedPositionCreateDto intendedPositionCreateDto) {
        intendedPositionService.create(intendedPositionCreateDto);
        return R.ok();
    }

    /**
     * 编辑求职意向
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid IntendedPositionModifyDto intendedPositionIncreaseDto) {
        intendedPositionService.modify(intendedPositionIncreaseDto);
        return R.ok();
    }

    /**
     * 删除求职意向
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        intendedPositionService.delete(id);
        return R.ok();
    }

}
