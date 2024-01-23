package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.MyRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.vo.internalrecommend.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 内部推荐表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InternalRecommendMapper extends BaseMapper<InternalRecommend> {

    /**
     * 查询用户被推荐职位数量
     *
     * @param userId
     * @return
     */
    int queryListCount(@Param("userId") Long userId);

    /**
     * 查询用户被推荐职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<InternalRecommendVo> queryList(@Param("userId") Long userId, @Param("listDto") InternalRecommendQueryListDto listDto);

    /**
     * 查询我推荐的求职列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    MyRecommendVo queryMyRecommendList(@Param("userId") Long userId, @Param("listDto") MyRecommendQueryListDto listDto);

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    InternalRecommendDetailVo detail(@Param("id") Long id);

    /**
     * 查询我推荐的求职详情
     *
     * @param id
     * @return
     */
    MyRecommendDetailVo queryMyRecommendDetail(@Param("id") Long id);

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    List<InternalRecommendInterviewVo> queryInterviewList(@Param("listDto") InterviewQueryListDto listDto);

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    List<InternalRecommendOnboardingVo> queryOnboardingList(@Param("listDto") OnboardingQueryListDto listDto);

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    List<InternalRecommendConfirmationVo> queryConfirmationList(@Param("listDto") ConfirmationQueryListDto listDto);

    /**
     * 根据招聘流程实例id查找hr
     *
     * @param recruitProcessInstanceId
     * @return
     */
    Long selectHRByInstanceId(@Param("instanceId") Long recruitProcessInstanceId);

    /**
     * 根据招聘流程实例id查找候选人
     *
     * @param recruitProcessInstanceId
     * @return
     */
    Long selectApplicantByInstanceId(@Param("instanceId") Long recruitProcessInstanceId);
}
