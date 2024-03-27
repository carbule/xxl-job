package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.talentpool.*;
import com.korant.youya.workplace.pojo.vo.talentpool.*;

import java.math.BigDecimal;

/**
 * @ClassName TalentPoolService
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:37
 * @Version 1.0
 */
public interface TalentPoolService {

    /**
     * 查询人才库列表
     *
     * @param listDto
     * @return
     */
    Page<TalentPoolVo> queryList(TalentPoolQueryListDto listDto);

    /**
     * 查询人才详情
     *
     * @param id
     * @return
     */
    TalentDetailVo detail(Long id);

    /**
     * 查询人才招聘记录
     *
     * @param id
     * @return
     */
    TalentRecruitmentRecordsVo queryRecruitmentRecords(Long id);

    /**
     * 查询已发布职位
     *
     * @param listDto
     * @return
     */
    Page<PublishedJobVo> queryPublishedJobList(QueryPublishedJobListDto listDto);

    /**
     * 关联职位
     *
     * @param associateDto
     */
    void associate(AssociateDto associateDto);

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     */
    void createInterview(TalentPoolCreateInterviewDto createInterviewDto);

    /**
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolInterviewDetailVo interviewDetail(Long id);

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
    void createOnboarding(TalentPoolCreateOnboardingDto createOnboardingDto);

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolOnboardingDetailVo onboardingDetail(Long id);

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
    void createConfirmation(TalentPoolCreateConfirmationDto createConfirmationDto);

    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    TalentPoolConfirmationDetailVo confirmationDetail(Long id);

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
     * @param processType
     * @param desc
     */
    void freeze(Long enterpriseId, BigDecimal amount, Long jobId, int processType, String desc);

    /**
     * 企业钱包账户解冻
     *
     * @param enterpriseId
     * @param amount
     * @param jobId
     * @param processType
     * @param desc
     */
    void unfreeze(Long enterpriseId, BigDecimal amount, Long jobId, int processType, String desc);
}
