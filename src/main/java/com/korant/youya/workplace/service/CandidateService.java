package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateDetailVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateRecruitmentRecordsVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;
import com.korant.youya.workplace.pojo.vo.candidate.PublishedJobCategoryVo;

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
}
