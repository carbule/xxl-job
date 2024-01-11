package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.vo.applyjob.*;
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
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryInterviewList")
    public R<?> queryInterviewList(@RequestBody @Valid InterviewQueryListDto listDto) {
        Page<ApplyJobInterviewVo> page = applyJobService.queryInterviewList(listDto);
        return R.success(page);
    }

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryOnboardingList")
    public R<?> queryOnboardingList(@RequestBody @Valid OnboardingQueryListDto listDto) {
        Page<ApplyJobOnboardingVo> page = applyJobService.queryOnboardingList(listDto);
        return R.success(page);
    }

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryConfirmationList")
    public R<?> queryConfirmationList(@RequestBody @Valid ConfirmationQueryListDto listDto) {
        Page<ApplyJobConfirmationVo> page = applyJobService.queryConfirmationList(listDto);
        return R.success(page);
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
