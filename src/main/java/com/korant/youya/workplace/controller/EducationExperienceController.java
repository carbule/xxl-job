package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.educationexperience.EducationExperienceModifyDto;
import com.korant.youya.workplace.pojo.vo.educationexperience.EducationExperienceDetailVo;
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
     * 创建教育经历信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid EducationExperienceCreateDto createDto) {
        educationExperienceService.create(createDto);
        return R.ok();
    }

    /**
     * 修改教育经历信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid EducationExperienceModifyDto modifyDto) {
        educationExperienceService.modify(modifyDto);
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
        EducationExperienceDetailVo detailVo = educationExperienceService.detail(id);
        return R.success(detailVo);
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
