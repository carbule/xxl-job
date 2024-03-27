package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.AcceptanceStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.InternalRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.internalrecommend.MyRecommendQueryListDto;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.dto.msgsub.InterviewMsgSubDTO;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.internalrecommend.*;
import com.korant.youya.workplace.service.InternalRecommendService;
import com.korant.youya.workplace.service.WxService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 内部推荐表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class InternalRecommendServiceImpl extends ServiceImpl<InternalRecommendMapper, InternalRecommend> implements InternalRecommendService {

    @Resource
    private InternalRecommendMapper internalRecommendMapper;

    @Resource
    private InterviewMapper interviewMapper;

    @Resource
    private OnboardingMapper onboardingMapper;

    @Resource
    private ConfirmationMapper confirmationMapper;

    @Resource(name = "wxService4TalentPoolImpl")
    private WxService wxService;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HuntJobMapper huntJobMapper;

    /**
     * 查询用户被推荐职位列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<InternalRecommendVo> queryList(InternalRecommendQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count = internalRecommendMapper.queryListCount(userId);
        List<InternalRecommendVo> list = internalRecommendMapper.queryList(userId, listDto);
        Page<InternalRecommendVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询我推荐的求职列表
     *
     * @param listDto
     * @return
     */
    @Override
    public PageData<MyRecommendVo> queryMyRecommendList(MyRecommendQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = internalRecommendMapper.selectCount(new LambdaQueryWrapper<InternalRecommend>().eq(InternalRecommend::getReferee, userId).eq(InternalRecommend::getIsDelete, 0));
        MyRecommendVo myRecommendVo = internalRecommendMapper.queryMyRecommendList(userId, listDto);
        PageData<MyRecommendVo> page = new PageData<>();
        page.setData(myRecommendVo).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 查询用户被推荐职位详情
     *
     * @param id
     * @return
     */
    @Override
    public InternalRecommendDetailVo detail(Long id) {
        return internalRecommendMapper.detail(id);
    }

    /**
     * 查询我推荐的求职详情
     *
     * @param id
     * @return
     */
    @Override
    public MyRecommendDetailVo queryMyRecommendDetail(Long id) {
        return internalRecommendMapper.queryMyRecommendDetail(id);
    }

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<InternalRecommendInterviewVo> queryInterviewList(InterviewQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = interviewMapper.selectCount(new LambdaQueryWrapper<Interview>().eq(Interview::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Interview::getIsDelete, 0));
        List<InternalRecommendInterviewVo> list = internalRecommendMapper.queryInterviewList(listDto);
        Page<InternalRecommendInterviewVo> page = new Page<>();
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
    public Page<InternalRecommendOnboardingVo> queryOnboardingList(OnboardingQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = onboardingMapper.selectCount(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getIsDelete, 0));
        List<InternalRecommendOnboardingVo> list = internalRecommendMapper.queryOnboardingList(listDto);
        Page<InternalRecommendOnboardingVo> page = new Page<>();
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
    public Page<InternalRecommendConfirmationVo> queryConfirmationList(ConfirmationQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = confirmationMapper.selectCount(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getIsDelete, 0));
        List<InternalRecommendConfirmationVo> list = internalRecommendMapper.queryConfirmationList(listDto);
        Page<InternalRecommendConfirmationVo> page = new Page<>();
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
        Long applicant = internalRecommendMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
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
        InternalRecommend internalRecommend = internalRecommendMapper.selectOne(new LambdaQueryWrapper<InternalRecommend>()
                .eq(InternalRecommend::getRecruitProcessInstanceId, interview.getRecruitProcessInstanceId()));
        if (internalRecommend == null) {
            throw new YouyaException("找不到推荐人信息");
        }
        HuntJob huntJob = Optional.ofNullable(huntJobMapper.selectById(internalRecommend.getHuntId()))
                .orElseThrow(() -> new YouyaException("找不到推荐人的求职信息"));
        Job job = Optional.ofNullable(jobMapper.selectById(internalRecommend.getJobId()))
                .orElseThrow(() -> new YouyaException("找不到职位信息"));
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
     * 接受入职邀约
     *
     * @param id
     */
    @Override
    public void acceptOnboarding(Long id) {
        Onboarding onboarding = onboardingMapper.selectOne(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getId, id).eq(Onboarding::getIsDelete, 0));
        if (null == onboarding) throw new YouyaException("入职邀约信息不存在");
        Long recruitProcessInstanceId = onboarding.getRecruitProcessInstanceId();
        Long applicant = internalRecommendMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
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
        Long applicant = internalRecommendMapper.selectApplicantByInstanceId(recruitProcessInstanceId);
        if (ObjectUtils.notEqual(applicant, SpringSecurityUtil.getUserId())) throw new YouyaException("非法操作");
        Integer acceptanceStatus = confirmation.getAcceptanceStatus();
        if (AcceptanceStatusEnum.PENDING.getStatus() != acceptanceStatus)
            throw new YouyaException("只有待接受的转正邀约才可以操作");
        confirmation.setAcceptanceStatus(AcceptanceStatusEnum.ACCEPTED.getStatus());
        confirmationMapper.updateById(confirmation);
    }
}
