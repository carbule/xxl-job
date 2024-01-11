package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.vo.applyjob.*;

/**
 * <p>
 * 职位申请表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ApplyJobService extends IService<ApplyJob> {

    /**
     * 查询用户已申请职位列表
     *
     * @param listDto
     * @return
     */
    Page<ApplyJobVo> queryList(ApplyJobQueryListDto listDto);

    /**
     * 查询用户已申请职位详情
     *
     * @param id
     * @return
     */
    ApplyJobDetailVo detail(Long id);

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    Page<ApplyJobInterviewVo> queryInterviewList(InterviewQueryListDto listDto);

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    Page<ApplyJobOnboardingVo> queryOnboardingList(OnboardingQueryListDto listDto);

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    Page<ApplyJobConfirmationVo> queryConfirmationList(ConfirmationQueryListDto listDto);

    /**
     * 接受面试邀约
     *
     * @param id
     */
    void acceptInterview(Long id);

    /**
     * 接受入职邀约
     *
     * @param id
     */
    void acceptOnboarding(Long id);

    /**
     * 接受转正邀约
     *
     * @param id
     */
    void acceptConfirmation(Long id);
}
