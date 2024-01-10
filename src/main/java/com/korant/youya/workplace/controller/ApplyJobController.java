package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobDetailVo;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobVo;
import com.korant.youya.workplace.service.ApplyJobService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 职位申请表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/applyJob")
public class ApplyJobController {

    @Resource
    private ApplyJobService applyJobService;

    /**
     * 查询用户已申请职位列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    @ExplanationDict
    public R<?> queryList(@RequestBody @Valid ApplyJobQueryListDto listDto) {
        Page<ApplyJobVo> page = applyJobService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 查询用户已申请职位详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
    public R<?> detail(@PathVariable("id") Long id) {
        ApplyJobDetailVo detailVo = applyJobService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 接受面试邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/acceptInterview/{id}")
    public R<?> acceptInterview(@PathVariable("id") Long id) {
        applyJobService.acceptInterview(id);
        return R.ok();
    }

    /**
     * 接受入职邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/acceptOnboarding/{id}")
    public R<?> acceptOnboarding(@PathVariable("id") Long id) {
        applyJobService.acceptOnboarding(id);
        return R.ok();
    }

    /**
     * 接受转正邀约
     *
     * @param id
     * @return
     */
    @GetMapping("/acceptConfirmation/{id}")
    public R<?> acceptConfirmation(@PathVariable("id") Long id) {
        applyJobService.acceptConfirmation(id);
        return R.ok();
    }
}
