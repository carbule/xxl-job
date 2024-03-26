package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CandidateService
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:39
 * @Version 1.0
 */
public interface CandidateService {

    /**
     * 查询候选人列表
     *
     * @param listDto
     * @return
     */
    Page<CandidateVo> queryList(CandidateQueryListDto listDto);

    /**
     * 查询已发布职位分类
     *
     * @return
     */
    List<PublishedJobCategoryVo> queryPublishedJobCategoryList();

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    CandidateDetailVo detail(Long id);

    /**
     * 查询候选人招聘记录
     *
     * @param id
     * @return
     */
    CandidateRecruitmentRecordsVo queryRecruitmentRecords(Long id);

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     */
    void createInterview(CandidateCreateInterviewDto createInterviewDto);

    /**
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    CandidateInterviewDetailVo interviewDetail(Long id);

    /**
     * 取消面试邀约
     *
     * @param id
     */
    void cancelInterview(Long id);

    /**
     * 确认完成面试邀约
     *
     * @param id
     */
    void confirmInterview(Long id);

    /**
     * 删除面试邀约
     *
     * @param id
     */
    void deleteInterview(Long id);

    /**
     * 创建入职邀约
     *
     * @param createOnboardingDto
     */
    void createOnboarding(CandidateCreateOnboardingDto createOnboardingDto);

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    CandidateOnboardingDetailVo onboardingDetail(Long id);

    /**
     * 取消入职邀约
     *
     * @param id
     */
    void cancelOnboarding(Long id);

    /**
     * 确认完成入职邀约
     *
     * @param id
     */
    void confirmOnboarding(Long id);

    /**
     * 删除入职邀约
     *
     * @param id
     */
    void deleteOnboarding(Long id);

    /**
     * 创建转正邀约
     *
     * @param createConfirmationDto
     */
    void createConfirmation(CandidateCreateConfirmationDto createConfirmationDto);

    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    CandidateConfirmationDetailVo confirmationDetail(Long id);

    /**
     * 取消转正邀约
     *
     * @param id
     */
    void cancelConfirmation(Long id);

    /**
     * 确认完成转正邀约
     *
     * @param id
     */
    void confirmConfirmation(Long id);

    /**
     * 删除转正邀约
     *
     * @param id
     */
    void deleteConfirmation(Long id);

    /**
     * 企业钱包账户冻结
     *
     * @param enterpriseId
     * @param amount
     * @param jobId
     * @param desc
     */
    void freeze(Long enterpriseId, BigDecimal amount, Long jobId, String desc);

    /**
     * 企业钱包账户解冻
     *
     * @param enterpriseId
     * @param amount
     * @param jobId
     * @param desc
     */
    void unfreeze(Long enterpriseId, BigDecimal amount, Long jobId, String desc);
}
