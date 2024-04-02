package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.constants.RabbitConstant;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.*;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingProgressMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.mq.InterviewAppointmentMsgSubMessage;
import com.korant.youya.workplace.pojo.dto.talentpool.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.talentpool.*;
import com.korant.youya.workplace.service.TalentPoolService;
import com.korant.youya.workplace.service.WxService;
import com.korant.youya.workplace.utils.CalculationUtil;
import com.korant.youya.workplace.utils.IdGenerationUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TalentPoolServiceImpl
 * @Description
 * @Author chenyiqiang
 * @Date 2024/1/4 17:37
 * @Version 1.0
 */
@Service
@Slf4j
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

    @Resource
    private TalentPoolService talentPoolService;

    @Resource
    private EnterpriseWalletAccountMapper enterpriseWalletAccountMapper;

    @Resource
    private EnterpriseWalletFreezeRecordMapper enterpriseWalletFreezeRecordMapper;

    @Resource
    private WalletTransactionFlowMapper walletTransactionFlowMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource(name = "wxService4TalentPoolImpl")
    private WxService wxService;

    @Resource
    private HuntJobMapper huntJobMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private static final String JOB_INTERVIEW_UNFREEZE_DES = "面试取消";

    private static final String JOB_ONBOARD_FREEZE_DES = "入职";

    private static final String JOB_ONBOARD_UNFREEZE_DES = "入职取消";

    private static final String JOB_FULL_MEMBER_FREEZE_DES = "转正";

    private static final String JOB_FULL_MEMBER_UNFREEZE_DES = "转正取消";

    private static final String JOB_INTERVIEW_FREEZE_DES = "面试";

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
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal interviewRewardRate = job.getInterviewRewardRate();
            if (interviewRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = interviewRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    talentPoolService.freeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.INTERVIEW.getType(), JOB_INTERVIEW_FREEZE_DES);
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

        // 发送微信消息订阅事件
        sendInterviewMessageSubscribe(job, internalRecommend, interview);
        sendInterviewAppointmentMessageSubscribeEvent(job, internalRecommend, interview);
    }

    /**
     * 发送微信消息订阅
     *
     * @param interview 面试记录
     */
    protected void sendInterviewMessageSubscribe(Job job, InternalRecommend internalRecommend, Interview interview) {
        HuntJob huntJob = Optional.ofNullable(huntJobMapper.selectById(internalRecommend.getHuntId()))
                .orElseThrow(() -> new YouyaException("找不到推荐人的求职信息"));
        User hr = Optional.ofNullable(userMapper.selectById(internalRecommend.getHr()))
                .orElseThrow(() -> new YouyaException("找不到 HR 用户信息"));
        User user = Optional.ofNullable(userMapper.selectById(huntJob.getUid()))
                .orElseThrow(() -> new YouyaException("找不到求职人信息"));
        Enterprise enterprise = Optional.ofNullable(enterpriseMapper.selectById(job.getEnterpriseId()))
                .orElseThrow(() -> new YouyaException("找不到职位的企业信息"));

        wxService.sendInterviewMessageSubscribe(user.getWechatOpenId(), new InterviewMsgSubDTO()
                .setJobId(job.getId())
                .setInternalRecommendId(internalRecommend.getId())
                .setPositionName(job.getPositionName())
                .setEnterpriseName(enterprise.getName())
                .setTime(interview.getInterTime())
                .setLinkman(hr.getLastName() + hr.getFirstName()));
    }

    /**
     * 发送微信消息订阅时间
     *
     * @param job 职位
     * @param ir  内推记录
     * @param itv 面试记录
     */
    protected void sendInterviewAppointmentMessageSubscribeEvent(Job job, InternalRecommend ir, Interview itv) {
        HuntJob huntJob = Optional.ofNullable(huntJobMapper.selectById(ir.getHuntId()))
                .orElseThrow(() -> new YouyaException("找不到推荐人的求职信息"));
        User user = Optional.ofNullable(userMapper.selectById(huntJob.getUid()))
                .orElseThrow(() -> new YouyaException("找不到求职人的用户信息"));

        InterviewAppointmentMsgSubMessage messageBody = new InterviewAppointmentMsgSubMessage();
        messageBody.setJob(job);
        messageBody.setUser(user);
        messageBody.setInterview(itv);
        messageBody.setInternalRecommendId(ir.getId());

        // 发送到延迟队列
        rabbitTemplate.convertAndSend(
                RabbitConstant.Exchange.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_EXCHANGE,
                RabbitConstant.RoutingKey.WX_MESSAGE_SUBSCRIBE_INTERVIEW_APPOINTMENT_4TALENT_POOL_ROUTING_KEY,
                JSON.toJSONString(messageBody),
                message -> {
                    message.getMessageProperties().setDelay(24 * 60 * 60 * 1000);
                    return message;
                });
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
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
                    talentPoolService.unfreeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.INTERVIEW.getType(), JOB_INTERVIEW_UNFREEZE_DES);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
    public void createOnboarding(TalentPoolCreateOnboardingDto createOnboardingDto) {
        Long id = createOnboardingDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("请先关联职位");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal onboardRewardRate = job.getOnboardRewardRate();
            if (onboardRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = onboardRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    talentPoolService.freeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.ONBOARD.getType(), JOB_ONBOARD_FREEZE_DES);
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
        sendOnboardingMessageSubscribe(job, internalRecommend, onboarding);
    }

    /**
     * 发送微信消息订阅
     *
     * @param job               职位信息
     * @param internalRecommend 内推记录
     * @param onboarding        入职记录
     */
    protected void sendOnboardingMessageSubscribe(Job job, InternalRecommend internalRecommend, Onboarding onboarding) {
        HuntJob huntJob = Optional.ofNullable(huntJobMapper.selectById(internalRecommend.getHuntId()))
                .orElseThrow(() -> new YouyaException("找不到推荐人的求职信息"));
        User user = Optional.ofNullable(userMapper.selectById(huntJob.getUid()))
                .orElseThrow(() -> new YouyaException("找不到求职人的用户信息"));
        Enterprise enterprise = Optional.ofNullable(enterpriseMapper.selectById(job.getEnterpriseId()))
                .orElseThrow(() -> new YouyaException("找不到职位对应的公司信息"));

        wxService.sendOnboardingMessageSubscribe(user.getWechatOpenId(), new OnboardingMsgSubDTO()
                .setJobId(job.getId())
                .setInternalRecommendId(internalRecommend.getId())
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
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
                    talentPoolService.unfreeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.ONBOARD.getType(), JOB_ONBOARD_UNFREEZE_DES);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
    public void createConfirmation(TalentPoolCreateConfirmationDto createConfirmationDto) {
        Long id = createConfirmationDto.getId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getId, id).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("请先关联职位");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
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
        Long enterpriseId = loginUser.getEnterpriseId();
        BigDecimal award = job.getAward();
        if (null != award) {
            BigDecimal fullMemberRewardRate = job.getFullMemberRewardRate();
            if (fullMemberRewardRate.compareTo(new BigDecimal("0")) > 0) {
                BigDecimal rate = fullMemberRewardRate.multiply(new BigDecimal("0.01"));
                BigDecimal amount = CalculationUtil.multiply(award, rate, 0);
                if (amount.compareTo(new BigDecimal("0")) > 0) {
                    talentPoolService.freeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.FULL_MEMBER.getType(), JOB_FULL_MEMBER_FREEZE_DES);
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
        sendOnboardingProgressMessageSubscribe(job, internalRecommend, confirmation);
    }

    /**
     * 发送微信消息订阅
     *
     * @param job 职位信息
     * @param ir  内推记录
     * @param cfm 转正记录
     */
    protected void sendOnboardingProgressMessageSubscribe(Job job, InternalRecommend ir, Confirmation cfm) {
        HuntJob huntJob = Optional.ofNullable(huntJobMapper.selectById(ir.getHuntId()))
                .orElseThrow(() -> new YouyaException("找不到推荐人的求职信息"));
        User user = Optional.ofNullable(userMapper.selectById(huntJob.getUid()))
                .orElseThrow(() -> new YouyaException("找不到求职人的用户信息"));
        Enterprise enterprise = Optional.ofNullable(enterpriseMapper.selectById(job.getEnterpriseId()))
                .orElseThrow(() -> new YouyaException("找不到职位对应的公司信息"));

        wxService.sendOnboardingProgressMessageSubscribe(user.getWechatOpenId(), new OnboardingProgressMsgSubDTO()
                .setJobId(job.getId())
                .setInternalRecommendId(ir.getId())
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
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(InternalRecommend::getIsDelete, 0));
        if (null == internalRecommend) throw new YouyaException("人才信息不存在");
        Long jobId = internalRecommend.getJobId();
        if (null == jobId) throw new YouyaException("职位信息不存在");
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
                    talentPoolService.unfreeze(enterpriseId, amount, jobId, RecruitmentProcessEnum.FULL_MEMBER.getType(), JOB_FULL_MEMBER_UNFREEZE_DES);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
        Long hr = internalRecommendMapper.selectHRByInstanceId(recruitProcessInstanceId);
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
     * @param processType
     * @param desc
     */
    @Override
    public void freeze(Long enterpriseId, BigDecimal amount, Long jobId, int processType, String desc) {
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
                String freezeOrderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.ENTERPRISE.getCode(), YYBusinessCode.ENTERPRISE_FREEZE_OR_UNFREEZE.getCode());
                enterpriseWalletFreezeRecord.setFreezeOrderId(freezeOrderId);
                enterpriseWalletFreezeRecord.setEnterpriseWalletId(walletAccountId);
                enterpriseWalletFreezeRecord.setJobId(jobId);
                enterpriseWalletFreezeRecord.setProcessType(processType);
                enterpriseWalletFreezeRecord.setAmount(amount);
                enterpriseWalletFreezeRecord.setType(WalletFreezeTypeEnum.FREEZE.getType());
                enterpriseWalletFreezeRecord.setOperateTime(now);
                enterpriseWalletFreezeRecordMapper.insert(enterpriseWalletFreezeRecord);
                BigDecimal freezeAmount = walletAccount.getFreezeAmount();
                walletAccount.setFreezeAmount(freezeAmount.add(amount));
                walletAccount.setAvailableBalance(shortfall);
                enterpriseWalletAccountMapper.updateById(walletAccount);
                WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                String transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.USER_FREEZE_OR_UNFREEZE.getCode());
                walletTransactionFlow.setTransactionId(transactionFlowId).setAccountId(walletAccountId).setOrderId(freezeOrderId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.DEBIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
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
     * @param processType
     * @param desc
     */
    @Override
    public void unfreeze(Long enterpriseId, BigDecimal amount, Long jobId, int processType, String desc) {
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
                String freezeOrderId = IdGenerationUtil.generateOrderId(YYConsumerCodeEnum.ENTERPRISE.getCode(), YYBusinessCode.ENTERPRISE_FREEZE_OR_UNFREEZE.getCode());
                enterpriseWalletFreezeRecord.setFreezeOrderId(freezeOrderId);
                enterpriseWalletFreezeRecord.setEnterpriseWalletId(walletAccountId);
                enterpriseWalletFreezeRecord.setJobId(jobId);
                enterpriseWalletFreezeRecord.setProcessType(processType);
                enterpriseWalletFreezeRecord.setAmount(amount);
                enterpriseWalletFreezeRecord.setType(WalletFreezeTypeEnum.UNFREEZE.getType());
                enterpriseWalletFreezeRecord.setOperateTime(now);
                enterpriseWalletFreezeRecordMapper.insert(enterpriseWalletFreezeRecord);
                walletAccount.setFreezeAmount(freezeAmount.subtract(amount));
                walletAccount.setAvailableBalance(availableBalance.add(amount));
                enterpriseWalletAccountMapper.updateById(walletAccount);
                WalletTransactionFlow walletTransactionFlow = new WalletTransactionFlow();
                String transactionFlowId = IdGenerationUtil.generateTransactionFlowId(YYBusinessCode.USER_FREEZE_OR_UNFREEZE.getCode());
                walletTransactionFlow.setTransactionId(transactionFlowId).setAccountId(walletAccountId).setOrderId(freezeOrderId).setTransactionType(TransactionTypeEnum.FREEZE_OR_UNFREEZE.getType()).setTransactionDirection(TransactionDirectionTypeEnum.CREDIT.getType()).setAmount(amount).setCurrency(CurrencyTypeEnum.CNY.getType())
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
