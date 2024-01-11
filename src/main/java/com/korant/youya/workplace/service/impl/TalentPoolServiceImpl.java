package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.enums.CompletionStatusEnum;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.talentpool.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.talentpool.*;
import com.korant.youya.workplace.service.TalentPoolService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName TalentPoolServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:37
 * @Version 1.0
 */
@Service
public class TalentPoolServiceImpl implements TalentPoolService {

    @Resource
    private TalentPoolMapper talentPoolMapper;

    @Resource
    private InternalRecommendMapper internalRecommendMapper;

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
     * 查询人才库列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<TalentPoolVo> queryList(TalentPoolQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = talentPoolMapper.queryListCount(userId);
        List<TalentPoolVo> list = talentPoolMapper.queryList(userId, listDto);
        Page<TalentPoolVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询人才详情
     *
     * @param id
     * @return
     */
    @Override
    public TalentDetailVo detail(Long id) {
        return talentPoolMapper.detail(id);
    }

    /**
     * 查询人才招聘记录
     *
     * @param id
     * @return
     */
    @Override
    public TalentRecruitmentRecordsVo queryRecruitmentRecords(Long id) {
        return talentPoolMapper.queryRecruitmentRecords(id);
    }

    /**
     * 查询已发布职位
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<PublishedJobVo> queryPublishedJobList(QueryPublishedJobListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = talentPoolMapper.queryPublishedJobListCount(userId);
        List<PublishedJobVo> list = talentPoolMapper.queryPublishedJobList(userId, listDto);
        Page<PublishedJobVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 关联职位
     *
     * @param associateDto
     */
    @Override
    public void associate(AssociateDto associateDto) {
        Long id = associateDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long hr = internalRecommend.getHr();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        if (!loginUser.getId().equals(hr)) throw new YouyaException("非法操作");
        Long jobId = internalRecommend.getJobId();
        if (null != jobId) throw new YouyaException("请勿重复关联职位");
        jobId = associateDto.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Integer status = job.getStatus();
        Integer auditStatus = job.getAuditStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() != status || JobAuditStatusEnum.AUDIT_SUCCESS.getStatus() != auditStatus) {
            throw new YouyaException("职位暂未发布");
        }
        internalRecommend.setJobId(jobId);
        internalRecommendMapper.updateById(internalRecommend);
    }

    /**
     * 创建面试邀约
     *
     * @param createInterviewDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createInterview(TalentPoolCreateInterviewDto createInterviewDto) {
        Long id = createInterviewDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("请先关联职位");
        Long userId = SpringSecurityUtil.getUserId();
        Long hr = internalRecommend.getHr();
        if (!hr.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = internalRecommend.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            internalRecommend.setRecruitProcessInstanceId(recruitProcessInstanceId);
            internalRecommendMapper.updateById(internalRecommend);
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
     * 查询面试邀约详情
     *
     * @param id
     * @return
     */
    @Override
    public TalentPoolInterviewDetailVo interviewDetail(Long id) {
        return talentPoolMapper.interviewDetail(id);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
    @Transactional(rollbackFor = Exception.class)
    public void createOnboarding(TalentPoolCreateOnboardingDto createOnboardingDto) {
        Long id = createOnboardingDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("请先关联职位");
        Long userId = SpringSecurityUtil.getUserId();
        Long hr = internalRecommend.getHr();
        if (!hr.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = internalRecommend.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            internalRecommend.setRecruitProcessInstanceId(recruitProcessInstanceId);
            internalRecommendMapper.updateById(internalRecommend);
        } else {
            recruitProcessInstance = recruitProcessInstanceMapper.selectOne(new LambdaQueryWrapper<RecruitProcessInstance>().eq(RecruitProcessInstance::getId, recruitProcessInstanceId).eq(RecruitProcessInstance::getIsDelete, 0));
            if (null == recruitProcessInstance) throw new YouyaException("招聘环节实列不存在");
            Integer processStep = recruitProcessInstance.getProcessStep();
            if (processStep > 2) throw new YouyaException("当前招聘环节实列已更新至最新节点，无法创建入职邀请");
        }
        boolean e1 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getAcceptanceStatus, 0).eq(Onboarding::getCompletionStatus, 0));
        boolean e2 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getAcceptanceStatus, 2).eq(Onboarding::getCompletionStatus, 2));
        if (e1 || e2) throw new YouyaException("当前已存在入职邀请或入职邀请已完成，无法重复创建");
        Onboarding onboarding = new Onboarding();
        onboarding.setRecruitProcessInstanceId(recruitProcessInstanceId).setOnboardingTime(createOnboardingDto.getOnboardingTime()).setCountryCode(createOnboardingDto.getCountryCode()).setProvinceCode(createOnboardingDto.getProvinceCode()).setCityCode(createOnboardingDto.getCityCode()).setAddress(createOnboardingDto.getAddress()).setNote(createOnboardingDto.getNote());
        onboarding.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        onboardingMapper.insert(onboarding);
        if (recruitProcessInstance.getProcessStep() != 2) {
            recruitProcessInstance.setProcessStep(2);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }
    }

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    @Override
    public TalentPoolOnboardingDetailVo onboardingDetail(Long id) {
        return talentPoolMapper.onboardingDetail(id);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
    @Transactional(rollbackFor = Exception.class)
    public void createConfirmation(TalentPoolCreateConfirmationDto createConfirmationDto) {
        Long id = createConfirmationDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("请先关联职位");
        Long userId = SpringSecurityUtil.getUserId();
        Long hr = internalRecommend.getHr();
        if (!hr.equals(userId)) throw new YouyaException("非法操作");
        Long recruitProcessInstanceId = internalRecommend.getRecruitProcessInstanceId();
        RecruitProcessInstance recruitProcessInstance = null;
        if (null == recruitProcessInstanceId) {
            recruitProcessInstance = new RecruitProcessInstance();
            recruitProcessInstance.setProcessStep(0);
            recruitProcessInstanceMapper.insert(recruitProcessInstance);
            recruitProcessInstanceId = recruitProcessInstance.getId();
            internalRecommend.setRecruitProcessInstanceId(recruitProcessInstanceId);
            internalRecommendMapper.updateById(internalRecommend);
        } else {
            recruitProcessInstance = recruitProcessInstanceMapper.selectOne(new LambdaQueryWrapper<RecruitProcessInstance>().eq(RecruitProcessInstance::getId, recruitProcessInstanceId).eq(RecruitProcessInstance::getIsDelete, 0));
            if (null == recruitProcessInstance) throw new YouyaException("招聘环节实列不存在");
        }
        boolean e1 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getAcceptanceStatus, 0).eq(Confirmation::getCompletionStatus, 0));
        boolean e2 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getAcceptanceStatus, 2).eq(Confirmation::getCompletionStatus, 2));
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
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    @Override
    public TalentPoolConfirmationDetailVo confirmationDetail(Long id) {
        return talentPoolMapper.confirmationDetail(id);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = confirmation.getCompletionStatus();
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus) throw new YouyaException("只有已取消的转正邀约才能删除");
        confirmation.setIsDelete(1);
        confirmationMapper.updateById(confirmation);
    }
}
