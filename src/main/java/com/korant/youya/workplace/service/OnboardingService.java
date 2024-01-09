package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.Onboarding;
import com.korant.youya.workplace.pojo.vo.onboarding.OnboardingVo;

/**
 * <p>
 * 入职记录表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface OnboardingService extends IService<Onboarding> {

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    Page<OnboardingVo> queryList(OnboardingQueryListDto listDto);
}
