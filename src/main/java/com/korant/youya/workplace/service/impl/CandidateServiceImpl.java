package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.enums.CompletionStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateDetailVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateRecruitmentRecordsVo;
import com.korant.youya.workplace.pojo.vo.candidate.CandidateVo;
import com.korant.youya.workplace.pojo.vo.candidate.PublishedJobCategoryVo;
import com.korant.youya.workplace.service.CandidateService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CandidateServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:40
 * @Version 1.0
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    @Resource
    private CandidateMapper candidateMapper;

    @Resource
    private ApplyJobMapper applyJobMapper;

    @Resource
    private RecruitProcessInstanceMapper recruitProcessInstanceMapper;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private InterviewMapper interviewMapper;

    @Resource
    private OnboardingMapper onboardingMapper;

    @Resource
    private ConfirmationMapper confirmationMapper;

    /**
     * 查询候选人列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<CandidateVo> queryList(CandidateQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = candidateMapper.queryListCount(userId, listDto);
        List<CandidateVo> list = candidateMapper.queryList(userId, listDto);
        Page<CandidateVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询已发布职位分类
     *
     * @return
     */
    @Override
    public List<PublishedJobCategoryVo> queryPublishedJobCategoryList() {
        Long userId = SpringSecurityUtil.getUserId();
        return candidateMapper.queryPublishedJobCategoryList(userId);
    }

    /**
     * 查询候选人详情
     *
     * @param id
     * @return
     */
    @Override
    public CandidateDetailVo detail(Long id) {
        return candidateMapper.detail(id);
    }

    /**
     * 查询候选人招聘记录
     *
     * @param id
     * @return
     */
    @Override
    public CandidateRecruitmentRecordsVo queryRecruitmentRecords(Long id) {
        return candidateMapper.queryRecruitmentRecords(id);
    }

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createInterview(CandidateCreateInterviewDto createInterviewDto) {
        Long id = createInterviewDto.getId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getId, id).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = applyJob.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            applyJob.setRecruitProcessInstanceId(recruitProcessInstanceId);
            applyJobMapper.updateById(applyJob);
        } else {
            recruitProcessInstance = recruitProcessInstanceMapper.selectOne(new LambdaQueryWrapper<RecruitProcessInstance>().eq(RecruitProcessInstance::getId, recruitProcessInstanceId).eq(RecruitProcessInstance::getIsDelete, 0));
            if (null == recruitProcessInstance) throw new YouyaException("招聘环节实列不存在");
            Integer processStep = recruitProcessInstance.getProcessStep();
            if (processStep > 1) throw new YouyaException("当前招聘环节实列已更新至最新节点，无法创建面试邀请");
        }
        Interview interview = new Interview();
        interview.setRecruitProcessInstanceId(recruitProcessInstanceId).setApproach(createInterviewDto.getApproach()).setInterTime(createInterviewDto.getInterTime()).setNote(createInterviewDto.getNote());
        interview.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        interviewMapper.insert(interview);
        if (recruitProcessInstance.getProcessStep() != 1) {
            recruitProcessInstance.setProcessStep(1);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }
    }

    /**
     * 取消面试邀约
     *
     * @param id
     */
    @Override
    public void cancelInterview(Long id) {
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = interview.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus) throw new YouyaException("只有未完成的面试邀约才能取消");
        interview.setCompletionStatus(CompletionStatusEnum.CANCELED.getStatus());
        interviewMapper.updateById(interview);
    }

    /**
     * 确认完成面试邀约
     *
     * @param id
     */
    @Override
    public void confirmInterview(Long id) {
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = interview.getAcceptanceStatus();
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus) throw new YouyaException("请等待受邀人完成接受操作");
        interview.setCompletionStatus(CompletionStatusEnum.COMPLETE.getStatus());
        interviewMapper.updateById(interview);
    }

    /**
     * 删除面试邀约
     *
     * @param id
     */
    @Override
    public void deleteInterview(Long id) {
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = interview.getCompletionStatus();
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus) throw new YouyaException("只有已取消的面试邀约才能删除");
        interview.setIsDelete(1);
        interviewMapper.updateById(interview);
    }

    /**
     * 创建入职邀约
     *
     * @param createOnboardingDto
     */
    @Override
    public void createOnboarding(CandidateCreateOnboardingDto createOnboardingDto) {
        Long id = createOnboardingDto.getId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getId, id).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = applyJob.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            applyJob.setRecruitProcessInstanceId(recruitProcessInstanceId);
            applyJobMapper.updateById(applyJob);
        } else {
            recruitProcessInstance = recruitProcessInstanceMapper.selectOne(new LambdaQueryWrapper<RecruitProcessInstance>().eq(RecruitProcessInstance::getId, recruitProcessInstanceId).eq(RecruitProcessInstance::getIsDelete, 0));
            if (null == recruitProcessInstance) throw new YouyaException("招聘环节实列不存在");
            Integer processStep = recruitProcessInstance.getProcessStep();
            if (processStep > 2) throw new YouyaException("当前招聘环节实列已更新至最新节点，无法创建入职邀请");
        }
        boolean e1 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstance).eq(Onboarding::getAcceptanceStatus, 0).eq(Onboarding::getCompletionStatus, 0));
        boolean e2 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstance).eq(Onboarding::getAcceptanceStatus, 2).eq(Onboarding::getCompletionStatus, 2));
        if (e1 || e2) throw new YouyaException("当前已存在入职邀请或入职邀请已完成，无法重复创建");
        Onboarding onboarding = new Onboarding();
        onboarding.setRecruitProcessInstanceId(recruitProcessInstanceId).setOnboardingTime(createOnboardingDto.getOnboardingTime()).setAddress(createOnboardingDto.getAddress()).setNote(createOnboardingDto.getNote());
        onboarding.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        onboardingMapper.insert(onboarding);
        if (recruitProcessInstance.getProcessStep() != 2) {
            recruitProcessInstance.setProcessStep(2);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }
    }

    /**
     * 取消入职邀约
     *
     * @param id
     */
    @Override
    public void cancelOnboarding(Long id) {
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = onboarding.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus) throw new YouyaException("只有未完成的入职邀约才能取消");
        onboarding.setCompletionStatus(CompletionStatusEnum.CANCELED.getStatus());
        onboardingMapper.updateById(onboarding);
    }

    /**
     * 确认完成入职邀约
     *
     * @param id
     */
    @Override
    public void confirmOnboarding(Long id) {
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = onboarding.getAcceptanceStatus();
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus) throw new YouyaException("请等待受邀人完成接受操作");
        onboarding.setCompletionStatus(CompletionStatusEnum.COMPLETE.getStatus());
        onboardingMapper.updateById(onboarding);
    }

    /**
     * 删除入职邀约
     *
     * @param id
     */
    @Override
    public void deleteOnboarding(Long id) {
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = onboarding.getCompletionStatus();
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus) throw new YouyaException("只有已取消的入职邀约才能删除");
        onboarding.setIsDelete(1);
        onboardingMapper.updateById(onboarding);
    }

    /**
     * 创建转正邀约
     *
     * @param createConfirmationDto
     */
    @Override
    public void createConfirmation(CandidateCreateConfirmationDto createConfirmationDto) {
        Long id = createConfirmationDto.getId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getId, id).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = applyJob.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            applyJob.setRecruitProcessInstanceId(recruitProcessInstanceId);
            applyJobMapper.updateById(applyJob);
        } else {
            recruitProcessInstance = recruitProcessInstanceMapper.selectOne(new LambdaQueryWrapper<RecruitProcessInstance>().eq(RecruitProcessInstance::getId, recruitProcessInstanceId).eq(RecruitProcessInstance::getIsDelete, 0));
            if (null == recruitProcessInstance) throw new YouyaException("招聘环节实列不存在");
        }
        boolean e1 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstance).eq(Confirmation::getAcceptanceStatus, 0).eq(Confirmation::getCompletionStatus, 0));
        boolean e2 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstance).eq(Confirmation::getAcceptanceStatus, 2).eq(Confirmation::getCompletionStatus, 2));
        if (e1 || e2) throw new YouyaException("当前已存在转正邀请或转正邀请已完成，无法重复创建");
        Confirmation confirmation = new Confirmation();
        confirmation.setRecruitProcessInstanceId(recruitProcessInstanceId).setConfirmationTime(createConfirmationDto.getConfirmationTime()).setSalary(createConfirmationDto.getSalary()).setNote(createConfirmationDto.getNote());
        confirmation.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        confirmationMapper.insert(confirmation);
        if (recruitProcessInstance.getProcessStep() != 3) {
            recruitProcessInstance.setProcessStep(3);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }
    }

    /**
     * 取消转正邀约
     *
     * @param id
     */
    @Override
    public void cancelConfirmation(Long id) {
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = confirmation.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus) throw new YouyaException("只有未完成的转正邀约才能取消");
        confirmation.setCompletionStatus(CompletionStatusEnum.CANCELED.getStatus());
        confirmationMapper.updateById(confirmation);
    }

    /**
     * 确认完成转正邀约
     *
     * @param id
     */
    @Override
    public void confirmConfirmation(Long id) {
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = confirmation.getAcceptanceStatus();
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus) throw new YouyaException("请等待受邀人完成接受操作");
        confirmation.setCompletionStatus(CompletionStatusEnum.COMPLETE.getStatus());
        confirmationMapper.updateById(confirmation);
    }

    /**
     * 删除转正邀约
     *
     * @param id
     */
    @Override
    public void deleteConfirmation(Long id) {
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = confirmation.getCompletionStatus();
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus) throw new YouyaException("只有已取消的转正邀约才能删除");
        confirmation.setIsDelete(1);
        confirmationMapper.updateById(confirmation);
    }
}
