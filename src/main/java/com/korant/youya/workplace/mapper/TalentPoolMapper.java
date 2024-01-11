package com.korant.youya.workplace.mapper;

import com.korant.youya.workplace.pojo.dto.talentpool.QueryPublishedJobListDto;
import com.korant.youya.workplace.pojo.dto.talentpool.TalentPoolQueryListDto;
import com.korant.youya.workplace.pojo.vo.talentpool.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName TalentPoolMapper
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:39
 * @Version 1.0
 */
public interface TalentPoolMapper {

    /**
     * 查询人才库数量
     *
     * @param userId
     * @return
     */
    int queryListCount(@Param("userId") Long userId);

    /**
     * 查询人才库列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<TalentPoolVo> queryList(@Param("userId") Long userId, @Param("listDto") TalentPoolQueryListDto listDto);

    /**
     * 查询人才详情
     *
     * @param id
     * @return
     */
    TalentDetailVo detail(@Param("id") Long id);

    /**
     * 查询人才招聘记录
     *
     * @param id
     * @return
     */
    TalentRecruitmentRecordsVo queryRecruitmentRecords(@Param("id") Long id);

    /**
     * 查询已发布职位数量
     *
     * @param userId
     * @return
     */
    int queryPublishedJobListCount(@Param("userId") Long userId);

    /**
     * 查询已发布职位列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<PublishedJobVo> queryPublishedJobList(@Param("userId") Long userId, @Param("listDto") QueryPublishedJobListDto listDto);

    /**
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolInterviewDetailVo interviewDetail(@Param("id") Long id);

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolOnboardingDetailVo onboardingDetail(@Param("id") Long id);

    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolConfirmationDetailVo confirmationDetail(@Param("id") Long id);
}
