package com.korant.youya.workplace.mapper;

import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CandidateMapper
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 17:05
 * @Version 1.0
 */
public interface CandidateMapper {

    /**
     * 查询候选人数量
     *
     * @param userId
     * @param listDto
     * @return
     */
    int queryListCount(@Param("userId") Long userId, @Param("listDto") CandidateQueryListDto listDto);

    /**
     * 查询候选人列表
     *
     * @param userId
     * @param listDto
     * @return
     */
    List<CandidateVo> queryList(@Param("userId") Long userId, @Param("listDto") CandidateQueryListDto listDto);

    /**
     * 查询已发布职位分类
     *
     * @param userId
     * @return
     */
    List<PublishedJobCategoryVo> queryPublishedJobCategoryList(@Param("userId") Long userId);

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    CandidateDetailVo detail(@Param("id") Long id);

    /**
     * 查询候选人招聘记录
     *
     * @param id
     * @return
     */
    CandidateRecruitmentRecordsVo queryRecruitmentRecords(@Param("id") Long id);

    /**
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    CandidateInterviewDetailVo interviewDetail(@Param("id") Long id);

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    CandidateOnboardingDetailVo onboardingDetail(@Param("id") Long id);

    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    CandidateConfirmationDetailVo confirmationDetail(@Param("id") Long id);
}
