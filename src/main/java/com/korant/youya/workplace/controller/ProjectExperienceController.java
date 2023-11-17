package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.projectexperience.ProjectExperienceQueryListDto;
import com.korant.youya.workplace.pojo.vo.projectexperience.ProjectExperienceListVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.service.ProjectExperienceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 项目经验表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/projectExperience")
public class ProjectExperienceController {

    @Resource
    private ProjectExperienceService projectExperienceService;

    /**
     * 查询项目经验信息列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid ProjectExperienceQueryListDto listDto) {
        Page<ProjectExperienceListVo> page = projectExperienceService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 创建项目经验信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid ProjectExperienceCreateDto projectExperienceCreateDto) {
        projectExperienceService.create(projectExperienceCreateDto);
        return R.ok();
    }

    /**
     * 修改项目经验信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid ProjectExperienceModifyDto projectExperienceModifyDto) {
        projectExperienceService.modify(projectExperienceModifyDto);
        return R.ok();
    }

    /**
     * 查询项目经验信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        WorkExperienceDetailVo workExperienceDetailVo = projectExperienceService.detail(id);
        return R.success(workExperienceDetailVo);
    }

    /**
     * 删除项目经验信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        projectExperienceService.delete(id);
        return R.ok();
    }

}
