package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedworkarea.ExpectedWorkAreaModifyDto;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.ExpectedWorkAreaInfoVo;
import com.korant.youya.workplace.service.ExpectedWorkAreaService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户期望工作区域表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/expectedWorkArea")
public class ExpectedWorkAreaController {

    @Resource
    private ExpectedWorkAreaService expectedWorkAreaService;

    /**
     * 查询用户期望工作区域
     *
     * @return
     */
    @GetMapping("/queryList")
    public R<?> queryList() {
        List<ExpectedWorkAreaInfoVo> expectedWorkAreaInfoDtoList = expectedWorkAreaService.queryList();
        return R.success(expectedWorkAreaInfoDtoList);
    }

    /**
     * 添加期望工作区域
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid ExpectedWorkAreaCreateDto expectedWorkAreaCreateDto) {
        expectedWorkAreaService.create(expectedWorkAreaCreateDto);
        return R.ok();
    }

    /**
     * 编辑期望工作区域
     *
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid ExpectedWorkAreaModifyDto expectedWorkAreaModifyDto) {
        expectedWorkAreaService.modify(expectedWorkAreaModifyDto);
        return R.ok();
    }

    /**
     * 删除期望工作区域
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        expectedWorkAreaService.delete(id);
        return R.ok();
    }

}
