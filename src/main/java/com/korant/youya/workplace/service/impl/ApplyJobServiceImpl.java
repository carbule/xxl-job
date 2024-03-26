package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.msgsub.OnboardingMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.applyjob.*;
import com.korant.youya.workplace.service.ApplyJobService;
import com.korant.youya.workplace.service.WxService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 职位申请表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class ApplyJobServiceImpl extends ServiceImpl<ApplyJobMapper, ApplyJob> implements ApplyJobService {

    @Resource
    private ApplyJobMapper applyJobMapper;

    @Resource
    private InterviewMapper interviewMapper;

    @Resource
    private OnboardingMapper onboardingMapper;

    @Resource
    private ConfirmationMapper confirmationMapper;

    @Resource(name = "wxService4CandidateImpl")
    private WxService wxService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private JobMapper jobMapper;

    /**
     * 查询用户已申请职位列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ApplyJobVo> queryList(ApplyJobQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = applyJobMapper.selectCount(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getApplicant, userId).eq(ApplyJob::getIsDelete, 0));
        List<ApplyJobVo> list = applyJobMapper.queryList(userId, listDto);
        Page<ApplyJobVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询用户已申请职位详情
     *
     * @param id
     * @return
     */
    @Override
    public ApplyJobDetailVo detail(Long id) {
        return applyJobMapper.detail(id);
    }

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ApplyJobInterviewVo> queryInterviewList(InterviewQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = interviewMapper.selectCount(new LambdaQueryWrapper<Interview>().eq(Interview::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Interview::getIsDelete, 0));
        List<ApplyJobInterviewVo> list = applyJobMapper.queryInterviewList(listDto);
        Page<ApplyJobInterviewVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ApplyJobOnboardingVo> queryOnboardingList(OnboardingQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = onboardingMapper.selectCount(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getIsDelete, 0));
        List<ApplyJobOnboardingVo> list = applyJobMapper.queryOnboardingList(listDto);
        Page<ApplyJobOnboardingVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ApplyJobConfirmationVo> queryConfirmationList(ConfirmationQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = confirmationMapper.selectCount(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getIsDelete, 0));
        List<ApplyJobConfirmationVo> list = applyJobMapper.queryConfirmationList(listDto);
        Page<ApplyJobConfirmationVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 接受面试邀约
     *
     * @param id
     */
    @Override
    public void acceptInterview(Long id) {
        Interview interview = interviewMapper.selectOne(new LambdaQueryWrapper<Interview>().eq(Interview::getId, id).eq(Interview::getIsDelete, 0));
        if (null == interview) throw new YouyaException("面试邀约信息不存在");
        Long recruitProcessInstanceId = interview.getRecruitProcessInstanceId();
        Long applicant = applyJobMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(applicant, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = interview.getAcceptanceStatus();
        if (AcceptanceStatusEnum.PENDING.getStatus() != acceptanceStatus)
            throw new YouyaException("只有待接受的面试邀约才可以操作");
        interview.setAcceptanceStatus(AcceptanceStatusEnum.ACCEPTED.getStatus());
        interviewMapper.updateById(interview);

        // 发送微信消息订阅
        sendInterviewMessageSubscribe(interview);
    }

    /**
     * 发送微信消息订阅
     *
     * @param interview 面试记录
     */
    protected void sendInterviewMessageSubscribe(Interview interview) {
        ApplyJob applyJob = applyJobMapper.selectOne(new LambdaQueryWrapper<ApplyJob>()
                .eq(ApplyJob::getRecruitProcessInstanceId, interview.getRecruitProcessInstanceId()));
        if (applyJob == null) {
            throw new YouyaException("找不到职位申请信息");
        }
        Job job = Optional.ofNullable(jobMapper.selectById(applyJob.getJobId()))
                .orElseThrow(() -> new YouyaException("找不到申请的职位"));
        User hr = Optional.ofNullable(userMapper.selectById(job.getUid()))
                .orElseThrow(() -> new YouyaException("找不到职位的发布人信息"));
        User user = Optional.ofNullable(userMapper.selectById(applyJob.getApplicant()))
                .orElseThrow(() -> new YouyaException("找不到职位申请人信息"));
        Enterprise enterprise = Optional.ofNullable(enterpriseMapper.selectById(job.getEnterpriseId()))
                .orElseThrow(() -> new YouyaException("找不到职位对应的企业"));

        wxService.sendInterviewMessageSubscribe(user.getWechatOpenId(), new InterviewMsgSubDTO()
                .setJobId(job.getId())
                .setPositionName(job.getPositionName())
                .setEnterpriseName(enterprise.getName())
                .setTime(interview.getInterTime())
                .setLinkman(hr.getLastName() + hr.getFirstName()));
    }

    /**
     * 接受入职邀约
     *
     * @param id
     */
    @Override
    public void acceptOnboarding(Long id) {
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        Long applicant = applyJobMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(applicant, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = onboarding.getAcceptanceStatus();
        if (AcceptanceStatusEnum.PENDING.getStatus() != acceptanceStatus)
            throw new YouyaException("只有待接受的入职邀约才可以操作");
        onboarding.setAcceptanceStatus(AcceptanceStatusEnum.ACCEPTED.getStatus());
        onboardingMapper.updateById(onboarding);
    }

    /**
     * 接受转正邀约
     *
     * @param id
     */
    @Override
    public void acceptConfirmation(Long id) {
        Confirmation confirmation = confirmationMapper.selectOne(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getId, id).eq(Confirmation::getIsDelete, 0));
        if (null == confirmation) throw new YouyaException("转正邀约信息不存在");
        Long recruitProcessInstanceId = confirmation.getRecruitProcessInstanceId();
        Long applicant = applyJobMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(applicant, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = confirmation.getAcceptanceStatus();
        if (AcceptanceStatusEnum.PENDING.getStatus() != acceptanceStatus)
            throw new YouyaException("只有待接受的转正邀约才可以操作");
        confirmation.setAcceptanceStatus(AcceptanceStatusEnum.ACCEPTED.getStatus());
        confirmationMapper.updateById(confirmation);
    }
}
