package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.vo.applyjob.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 职位申请表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface ApplyJobMapper extends BaseMapper<ApplyJob> {

    /**
     * 查询用户已申请职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<ApplyJobVo> queryList(@Param("userId") Long userId, @Param("listDto") ApplyJobQueryListDto listDto);

    /**
     * 查询用户已申请职位详情
     *
     * @param id
     * @return
     */
    ApplyJobDetailVo detail(@Param("id") Long id);

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    List<ApplyJobInterviewVo> queryInterviewList(@Param("listDto") InterviewQueryListDto listDto);

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    List<ApplyJobOnboardingVo> queryOnboardingList(@Param("listDto") OnboardingQueryListDto listDto);

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    List<ApplyJobConfirmationVo> queryConfirmationList(@Param("listDto") ConfirmationQueryListDto listDto);

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
