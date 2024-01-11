package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.vo.internalrecommend.*;
import com.korant.youya.workplace.service.InternalRecommendService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 内部推荐表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/internalRecommend")
public class InternalRecommendController {

    @Resource
    private InternalRecommendService internalRecommendService;

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    @ExplanationDict
    public R<?> queryList(@RequestBody @Valid InternalRecommendQueryListDto listDto) {
        Page<InternalRecommendVo> page = internalRecommendService.queryList(listDto);
        return R.success(page);
    }

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ExplanationDict
    public R<?> detail(@PathVariable("id") Long id) {
        InternalRecommendDetailVo detailVo = internalRecommendService.detail(id);
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
        Page<InternalRecommendInterviewVo> page = internalRecommendService.queryInterviewList(listDto);
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
        Page<InternalRecommendOnboardingVo> page = internalRecommendService.queryOnboardingList(listDto);
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
        Page<InternalRecommendConfirmationVo> page = internalRecommendService.queryConfirmationList(listDto);
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
        internalRecommendService.acceptInterview(id);
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
        internalRecommendService.acceptOnboarding(id);
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
        internalRecommendService.acceptConfirmation(id);
        return R.ok();
    }
}
