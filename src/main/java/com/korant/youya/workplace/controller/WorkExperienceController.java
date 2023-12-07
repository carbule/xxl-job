package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceCreateDto;
import com.korant.youya.workplace.pojo.dto.workexperience.WorkExperienceModifyDto;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperienceDetailVo;
import com.korant.youya.workplace.pojo.vo.workexperience.WorkExperiencePersonalVo;
import com.korant.youya.workplace.service.WorkExperienceService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 查询个人工作履历列表
     *
     * @return
     */
    @GetMapping("/queryPersonalList")
    public R<?> queryPersonalList() {
        List<WorkExperiencePersonalVo> personalVoList = workExperienceService.queryPersonalList();
        return R.success(personalVoList);
    }

    /**
     * 创建工作履历信息
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid WorkExperienceCreateDto createDto) {
        workExperienceService.create(createDto);
        return R.ok();
    }

    /**
     * 修改工作履历信息
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid WorkExperienceModifyDto modifyDto) {
        workExperienceService.modify(modifyDto);
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
        WorkExperienceDetailVo detailVo = workExperienceService.detail(id);
        return R.success(detailVo);
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
