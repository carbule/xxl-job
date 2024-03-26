package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.*;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateConfirmationDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateInterviewDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateCreateOnboardingDto;
import com.korant.youya.workplace.pojo.dto.candidate.CandidateQueryListDto;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingProgressMsgSubDTO;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.candidate.*;
import com.korant.youya.workplace.service.CandidateService;
import com.korant.youya.workplace.service.WxService;
import com.korant.youya.workplace.utils.CalculationUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CandidateServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/3 16:40
 * @Version 1.0
 */
@Service
@Slf4j
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

    @Resource
    private CandidateService candidateService;

    @Resource
    private EnterpriseWalletAccountMapper enterpriseWalletAccountMapper;

    @Resource
    private EnterpriseWalletFreezeRecordMapper enterpriseWalletFreezeRecordMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource(name = "wxService4CandidateImpl")
    private WxService wxService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    private static final String JOB_INTERVIEW_FREEZE_DES = "面试";

    private static final String JOB_INTERVIEW_UNFREEZE_DES = "面试取消";

    private static final String JOB_ONBOARD_FREEZE_DES = "入职";

    private static final String JOB_ONBOARD_UNFREEZE_DES = "入职取消";

    private static final String JOB_FULL_MEMBER_FREEZE_DES = "转正";

    private static final String JOB_FULL_MEMBER_UNFREEZE_DES = "转正取消";

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
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal interviewRewardRate = job.getInterviewRewardRate();
            if (interviewRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = interviewRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.freeze(enterpriseId, amount, jobId, JOB_INTERVIEW_FREEZE_DES);
                }
            }
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
    public CandidateInterviewDetailVo interviewDetail(Long id) {
        return candidateMapper.interviewDetail(id);
    }

    /**
     * 取消面试邀约
     *
     * @param id
     */
    @Override
    public void cancelInterview(Long id) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = interview.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus)
            throw new YouyaException("只有未完成的面试邀约才能取消");
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal interviewRewardRate = job.getInterviewRewardRate();
            if (interviewRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = interviewRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.unfreeze(enterpriseId, amount, jobId, JOB_INTERVIEW_UNFREEZE_DES);
                }
            }
        }
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
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus)
            throw new YouyaException("请等待受邀人完成接受操作");
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
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus)
            throw new YouyaException("只有已取消的面试邀约才能删除");
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
    public void createOnboarding(CandidateCreateOnboardingDto createOnboardingDto) {
        Long id = createOnboardingDto.getId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getId, id).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        boolean e1 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getAcceptanceStatus, 0).eq(Onboarding::getCompletionStatus, 0));
        boolean e2 = onboardingMapper.exists(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getAcceptanceStatus, 2).eq(Onboarding::getCompletionStatus, 2));
        if (e1 || e2) throw new YouyaException("当前已存在入职邀请或入职邀请已完成，无法重复创建");
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal onboardRewardRate = job.getOnboardRewardRate();
            if (onboardRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = onboardRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.freeze(enterpriseId, amount, jobId, JOB_ONBOARD_FREEZE_DES);
                }
            }
        }
        Onboarding onboarding = new Onboarding();
        onboarding.setRecruitProcessInstanceId(recruitProcessInstanceId).setOnboardingTime(createOnboardingDto.getOnboardingTime()).setCountryCode(createOnboardingDto.getCountryCode()).setProvinceCode(createOnboardingDto.getProvinceCode()).setCityCode(createOnboardingDto.getCityCode()).setAddress(createOnboardingDto.getAddress()).setNote(createOnboardingDto.getNote());
        onboarding.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        onboardingMapper.insert(onboarding);
        if (recruitProcessInstance.getProcessStep() != 2) {
            recruitProcessInstance.setProcessStep(2);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }

        // 发送微信消息订阅
        sendOnboardingMessageSubscribe(job, applyJob, onboarding);
    }

    /**
     * 发送微信消息订阅
     *
     * @param job        职位
     * @param applyJob   职位申请记录
     * @param onboarding 入职记录
     */
    protected void sendOnboardingMessageSubscribe(Job job, ApplyJob applyJob, Onboarding onboarding) {
        // 查询申请人
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, applyJob.getApplicant()));
        if (user == null) {
            throw new YouyaException("用户不存在");
        }
        // 查询职位所在企业
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getId, job.getEnterpriseId()));
        if (enterprise == null) {
            throw new YouyaException("企业不存在");
        }
        wxService.sendOnboardingMessageSubscribe(user.getWechatOpenId(), new OnboardingMsgSubDTO()
                .setJobId(job.getId())
                .setPositionName(job.getPositionName())
                .setEnterpriseName(enterprise.getName())
                .setTime(onboarding.getOnboardingTime()));
    }

    /**
     * 查询入职邀约详情
     *
     * @param id
     * @return
     */
    @Override
    public CandidateOnboardingDetailVo onboardingDetail(Long id) {
        return candidateMapper.onboardingDetail(id);
    }

    /**
     * 取消入职邀约
     *
     * @param id
     */
    @Override
    public void cancelOnboarding(Long id) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = onboarding.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus)
            throw new YouyaException("只有未完成的入职邀约才能取消");
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal onboardRewardRate = job.getOnboardRewardRate();
            if (onboardRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = onboardRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.unfreeze(enterpriseId, amount, jobId, JOB_ONBOARD_UNFREEZE_DES);
                }
            }
        }
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
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus)
            throw new YouyaException("请等待受邀人完成接受操作");
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
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus)
            throw new YouyaException("只有已取消的入职邀约才能删除");
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
    public void createConfirmation(CandidateCreateConfirmationDto createConfirmationDto) {
        Long id = createConfirmationDto.getId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getId, id).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        boolean e1 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getAcceptanceStatus, 0).eq(Confirmation::getCompletionStatus, 0));
        boolean e2 = confirmationMapper.exists(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getAcceptanceStatus, 2).eq(Confirmation::getCompletionStatus, 2));
        if (e1 || e2) throw new YouyaException("当前已存在转正邀请或转正邀请已完成，无法重复创建");
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal fullMemberRewardRate = job.getFullMemberRewardRate();
            if (fullMemberRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = fullMemberRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.freeze(enterpriseId, amount, jobId, JOB_FULL_MEMBER_FREEZE_DES);
                }
            }
        }
        Confirmation confirmation = new Confirmation();
        confirmation.setRecruitProcessInstanceId(recruitProcessInstanceId).setConfirmationTime(createConfirmationDto.getConfirmationTime()).setSalary(createConfirmationDto.getSalary()).setNote(createConfirmationDto.getNote());
        confirmation.setAcceptanceStatus(AcceptanceStatusEnum.PENDING.getStatus()).setCompletionStatus(CompletionStatusEnum.INCOMPLETE.getStatus());
        confirmationMapper.insert(confirmation);
        if (recruitProcessInstance.getProcessStep() != 3) {
            recruitProcessInstance.setProcessStep(3);
            recruitProcessInstanceMapper.updateById(recruitProcessInstance);
        }

        // 发送微信消息订阅
        sendOnboardingProgressMessageSubscribe(job, applyJob, confirmation);
    }

    /**
     * 发送微信消息订阅
     *
     * @param job          职位
     * @param applyJob     职位申请信息
     * @param confirmation 转正信息
     */
    protected void sendOnboardingProgressMessageSubscribe(Job job, ApplyJob applyJob, Confirmation confirmation) {
        // 查询申请人
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, applyJob.getApplicant()));
        if (user == null) {
            throw new YouyaException("用户不存在");
        }
        // 查询职位所在企业
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getId, job.getEnterpriseId()));
        if (enterprise == null) {
            throw new YouyaException("企业不存在");
        }
        wxService.sendOnboardingProgressMessageSubscribe(user.getWechatOpenId(), new OnboardingProgressMsgSubDTO()
                .setJobId(job.getId())
                .setPositionName(job.getPositionName())
                .setEnterpriseName(enterprise.getName())
                .setProgress("转正待确认"));
    }


    /**
     * 查询转正邀约详情
     *
     * @param id
     * @return
     */
    @Override
    public CandidateConfirmationDetailVo confirmationDetail(Long id) {
        return candidateMapper.confirmationDetail(id);
    }

    /**
     * 取消转正邀约
     *
     * @param id
     */
    @Override
    public void cancelConfirmation(Long id) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(ApplyJob::getIsDelete, 0));
        if (null == applyJob) throw new YouyaException("候选人信息不存在");
        Long jobId = applyJob.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = applyJobMapper.selectHRByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(hr, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer completionStatus = confirmation.getCompletionStatus();
        if (CompletionStatusEnum.INCOMPLETE.getStatus() != completionStatus)
            throw new YouyaException("只有未完成的转正邀约才能取消");
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal fullMemberRewardRate = job.getFullMemberRewardRate();
            if (fullMemberRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = fullMemberRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    candidateService.unfreeze(enterpriseId, amount, jobId, JOB_FULL_MEMBER_UNFREEZE_DES);
                }
            }
        }
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
        if (AcceptanceStatusEnum.ACCEPTED.getStatus() != acceptanceStatus)
            throw new YouyaException("请等待受邀人完成接受操作");
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
        if (CompletionStatusEnum.CANCELED.getStatus() != completionStatus)
            throw new YouyaException("只有已取消的转正邀约才能删除");
        confirmation.setIsDelete(1);
        confirmationMapper.updateById(confirmation);
    }

    /**
     * 企业钱包账户冻结
     *
     * @param enterpriseId
     * @param amount
     * @param jobId
     * @param desc
     */
    public void freeze(Long enterpriseId, BigDecimal amount, Long jobId, String desc) {
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Long walletAccountId = enterpriseWalletAccountMapper.queryWalletIdByEnterpriseId(enterpriseId);
        String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
        RLock walletLock = redissonClient.getLock(walletLockKey);
        try {
            boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
            if (tryWalletLock) {
                EnterpriseWalletAccount walletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                if (null == walletAccount) throw new YouyaException("企业钱包账户不存在");
                Integer accountStatus = walletAccount.getStatus();
                if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("钱包账户已被冻结，请联系客服");
                BigDecimal availableBalance = walletAccount.getAvailableBalance();
                if (availableBalance.compareTo(new BigDecimal("0")) <= 0)
                    throw new YouyaException("当前账户可用余额为0，无法支付推荐奖励");
                BigDecimal shortfall = availableBalance.subtract(amount);
                if (shortfall.compareTo(new BigDecimal("0")) < 0) {
                    String msg = String.format("钱包账户余额：【%s】元，还差：【%s】元", availableBalance.multiply(new BigDecimal("0.01")), shortfall.abs().multiply(new BigDecimal("0.01")));
                    throw new YouyaException(msg);
                }
                LocalDateTime now = LocalDateTime.now();
                EnterpriseWalletFreezeRecord enterpriseWalletFreezeRecord = new EnterpriseWalletFreezeRecord();
                enterpriseWalletFreezeRecord.setEnterpriseWalletId(walletAccountId);
                enterpriseWalletFreezeRecord.setJobId(jobId);
                enterpriseWalletFreezeRecord.setAmount(amount);
                enterpriseWalletFreezeRecord.setType(WalletFreezeTypeEnum.FREEZE.getType());
                enterpriseWalletFreezeRecord.setOperateTime(now);
                enterpriseWalletFreezeRecordMapper.insert(enterpriseWalletFreezeRecord);
                BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                walletAccount.setFreezeAmount(freezeAmount.add(amount));
                walletAccount.setAvailableBalance(shortfall);
                enterpriseWalletAccountMapper.updateById(walletAccount);
                Long walletFreezeRecordId = enterpriseWalletFreezeRecord.getId();
                WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                walletTransactionFlow.setAccountId(walletAccountId).setOrderId(walletFreezeRecordId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
                        .setDescription(desc).setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(shortfall);
                walletTransactionFlowMapper.insert(walletTransactionFlow);
            } else {
                log.error("获取钱包账户锁超时");
                throw new YouyaException("网络异常，请稍后重试");
            }
        } catch (InterruptedException e) {
            log.error("获取钱包账户锁失败，原因：", e);
            throw new YouyaException("网络异常，请稍后重试");
        } catch (YouyaException e) {
            log.error("企业钱包账户冻结失败");
            throw e;
        } catch (Exception e) {
            log.error("企业钱包账户冻结失败，原因", e);
            throw new YouyaException("网络异常，请稍后重试");
        } finally {
            if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                walletLock.unlock();
            }
        }
    }

    /**
     * 企业钱包账户解冻
     *
     * @param enterpriseId
     * @param amount
     * @param jobId
     * @param desc
     */
    public void unfreeze(Long enterpriseId, BigDecimal amount, Long jobId, String desc) {
        if (null == enterpriseId) throw new YouyaException("当前账号未关联企业");
        Long walletAccountId = enterpriseWalletAccountMapper.queryWalletIdByEnterpriseId(enterpriseId);
        String walletLockKey = String.format(RedisConstant.YY_WALLET_ACCOUNT_LOCK, walletAccountId);
        RLock walletLock = redissonClient.getLock(walletLockKey);
        try {
            boolean tryWalletLock = walletLock.tryLock(3, TimeUnit.SECONDS);
            if (tryWalletLock) {
                EnterpriseWalletAccount walletAccount = enterpriseWalletAccountMapper.selectOne(new LambdaQueryWrapper<EnterpriseWalletAccount>().eq(EnterpriseWalletAccount::getEnterpriseId, enterpriseId).eq(EnterpriseWalletAccount::getIsDelete, 0));
                if (null == walletAccount) throw new YouyaException("企业钱包账户不存在");
                Integer accountStatus = walletAccount.getStatus();
                if (WalletAccountStatusEnum.FROZEN.getStatus() == accountStatus)
                    throw new YouyaException("钱包账户已被冻结，请联系客服");
                BigDecimal availableBalance = walletAccount.getAvailableBalance();
                BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                LocalDateTime now = LocalDateTime.now();
                EnterpriseWalletFreezeRecord enterpriseWalletFreezeRecord = new EnterpriseWalletFreezeRecord();
                enterpriseWalletFreezeRecord.setEnterpriseWalletId(walletAccountId);
                enterpriseWalletFreezeRecord.setJobId(jobId);
                enterpriseWalletFreezeRecord.setAmount(amount);
                enterpriseWalletFreezeRecord.setType(WalletFreezeTypeEnum.UNFREEZE.getType());
                enterpriseWalletFreezeRecord.setOperateTime(now);
                enterpriseWalletFreezeRecordMapper.insert(enterpriseWalletFreezeRecord);
                walletAccount.setFreezeAmount(freezeAmount.subtract(amount));
                walletAccount.setAvailableBalance(availableBalance.add(amount));
                enterpriseWalletAccountMapper.updateById(walletAccount);
                Long walletFreezeRecordId = enterpriseWalletFreezeRecord.getId();
                WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                walletTransactionFlow.setAccountId(walletAccountId).setOrderId(walletFreezeRecordId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
                        .setDescription(desc).setInitiationDate(now).setCompletionDate(now).setStatus(TransactionFlowStatusEnum.SUCCESSFUL.getStatus()).setTradeStatusDesc(TransactionFlowStatusEnum.SUCCESSFUL.getStatusDesc()).setBalanceBefore(availableBalance).setBalanceAfter(availableBalance.add(amount));
                walletTransactionFlowMapper.insert(walletTransactionFlow);
            } else {
                log.error("获取钱包账户锁超时");
                throw new YouyaException("网络异常，请稍后重试");
            }
        } catch (InterruptedException e) {
            log.error("获取钱包账户锁失败，原因：", e);
            throw new YouyaException("网络异常，请稍后重试");
        } catch (YouyaException e) {
            log.error("企业钱包账户解冻失败");
            throw e;
        } catch (Exception e) {
            log.error("企业钱包账户解冻失败，原因", e);
            throw new YouyaException("网络异常，请稍后重试");
        } finally {
            if (walletLock != null && walletLock.isHeldByCurrentThread()) {
                walletLock.unlock();
            }
        }
    }
}
