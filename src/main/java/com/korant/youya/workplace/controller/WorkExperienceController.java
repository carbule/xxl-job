package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceQueryListDto;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceListVo;
import com.korant.youya.workplace.service.WorkExperienceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 工作履历表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/workExperience")
public class WorkExperienceController {

    @Resource
    private WorkExperienceService workExperienceService;

    /**
     * 查询工作履历信息列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid WorkExperienceQueryListDto listDto) {
        Page<WorkExperienceListVo> page = workExperienceService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 创建工作履历信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid WorkExperienceCreateDto workExperienceCreateDto) {
        workExperienceService.create(workExperienceCreateDto);
        return R.ok();
    }

    /**
     * 修改工作履历信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid WorkExperienceModifyDto workExperienceModifyDto) {
        workExperienceService.modify(workExperienceModifyDto);
        return R.ok();
    }

    /**
     * 查询工作履历信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        WorkExperienceDetailVo workExperienceDetailVo = workExperienceService.detail(id);
        return R.success(workExperienceDetailVo);
    }

    /**
     * 删除工作履历信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        workExperienceService.delete(id);
        return R.ok();
    }
}
