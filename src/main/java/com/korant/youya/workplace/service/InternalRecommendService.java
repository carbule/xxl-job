package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.MyRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.vo.internalrecommend.*;

/**
 * <p>
 * 内部推荐表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InternalRecommendService extends IService<InternalRecommend> {

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    Page<InternalRecommendVo> queryList(InternalRecommendQueryListDto listDto);

    /**
     * 查询我推荐的求职列表
     *
     * @param listDto
     * @return
     */
    PageData<MyRecommendVo> queryMyRecommendList(MyRecommendQueryListDto listDto);

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    InternalRecommendDetailVo detail(Long id);

    /**
     * 查询我推荐的求职详情
     *
     * @param id
     * @return
     */
    MyRecommendDetailVo queryMyRecommendDetail(Long id);

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    Page<InternalRecommendInterviewVo> queryInterviewList(InterviewQueryListDto listDto);

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    Page<InternalRecommendOnboardingVo> queryOnboardingList(OnboardingQueryListDto listDto);

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    Page<InternalRecommendConfirmationVo> queryConfirmationList(ConfirmationQueryListDto listDto);

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
