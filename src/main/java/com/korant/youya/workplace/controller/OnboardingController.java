package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.vo.onboarding.OnboardingVo;
import com.korant.youya.workplace.service.OnboardingService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 入职记录表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    @Resource
    private OnboardingService onboardingService;

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    @PostMapping("/queryList")
    public R<?> queryList(@RequestBody @Valid OnboardingQueryListDto listDto) {
        Page<OnboardingVo> page = onboardingService.queryList(listDto);
        return R.success(page);
    }
}
