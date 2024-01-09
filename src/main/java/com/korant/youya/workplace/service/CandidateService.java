package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateDetailVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateRecruitmentRecordsVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;

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
     * 创建入职邀约
     *
     * @param createOnboardingDto
     */
    void createOnboarding(CandidateCreateOnboardingDto createOnboardingDto);

    /**
     * 创建转正邀约
     *
     * @param createConfirmationDto
     */
    void createConfirmation(CandidateCreateConfirmationDto createConfirmationDto);
}
