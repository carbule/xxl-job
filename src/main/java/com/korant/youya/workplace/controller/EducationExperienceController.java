package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceQueryListDto;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceListVo;
import com.korant.youya.workplace.service.EducationExperienceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 教育经历表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/educationExperience")
public class EducationExperienceController {
    
    @Resource
    private EducationExperienceService educationExperienceService;

    /**
     * 查询教育经历信息列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    @ExplanationDict
    public R<?> queryList(@RequestBody @Valid EducationExperienceQueryListDto listDto) {
        Page<EducationExperienceListVo> page = educationExperienceService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 创建教育经历信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid EducationExperienceCreateDto educationExperienceCreateDto) {
        educationExperienceService.create(educationExperienceCreateDto);
        return R.ok();
    }

    /**
     * 修改教育经历信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid EducationExperienceModifyDto educationExperienceModifyDto) {
        educationExperienceService.modify(educationExperienceModifyDto);
        return R.ok();
    }

    /**
     * 查询教育经历信息详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
    public R<?> detail(@PathVariable("id") Long id) {
        EducationExperienceDetailVo educationExperienceDetailVo = educationExperienceService.detail(id);
        return R.success(educationExperienceDetailVo);
    }

    /**
     * 删除教育经历信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        educationExperienceService.delete(id);
        return R.ok();
    }

}
