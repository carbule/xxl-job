package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobNewStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.Location;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.job.*;
import com.korant.youya.workplace.pojo.po.*;
import com.korant.youya.workplace.pojo.vo.job.*;
import com.korant.youya.workplace.pojo.vo.user.UserPublicInfoVo;
import com.korant.youya.workplace.service.JobService;
import com.korant.youya.workplace.utils.CalculationUtil;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.korant.youya.workplace.utils.TencentMapUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
@Slf4j
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Resource
    private JobMapper jobMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    @Resource
    private JobWelfareLabelMapper jobWelfareLabelMapper;

    @Resource
    private AttentionJobMapper attentionJobMapper;

    @Resource
    private ApplyJobMapper applyJobMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DistrictDataMapper districtDataMapper;

    @Resource
    private BonusDistributionRuleMapper bonusDistributionRuleMapper;

    private static final String CHINA_CODE = "100000";

    private static final String NANJING_CITY_CODE = "320100";

    private static final String JOB_INTERVIEW_PRODUCT = "job_interview";

    private static final String JOB_INTERVIEW_BONUS_DISTRIBUTION_RULE = "job_interview_rule";

    private static final String JOB_ONBOARD_PRODUCT = "job_onboard";

    private static final String JOB_ONBOARD_BONUS_DISTRIBUTION_RULE = "job_onboard_rule";

    private static final String JOB_FULL_MEMBER_PRODUCT = "job_full_member";

    private static final String JOB_FULL_MEMBER_BONUS_DISTRIBUTION_RULE = "job_full_member_rule";

    private static final String JOB_FREEZE_DES = "友涯企业职位奖金冻结";

    private static final String JOB_UNFREEZE_DES = "友涯企业职位奖金解冻";

    /**
     * 查询首页职位信息列表
     *
     * @param listDto
     * @param request
     * @return
     */
    @Override
    public Page<JobHomePageListVo> queryHomePageList(JobQueryHomePageListDto listDto, HttpServletRequest request) {
        String token = request.getHeader("token");
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count;
        List<JobHomePageListVo> list;
        if (StringUtils.isBlank(token)) {
            listDto.setCityCode(NANJING_CITY_CODE);
            count = jobMapper.queryHomePageListCount(listDto);
            list = jobMapper.queryHomePageList(listDto);
        } else {
            Long userId = null;
            try {
                userId = JwtUtil.getId(token);
            } catch (Exception e) {
                throw new YouyaException("token校验失败");
            }
            count = jobMapper.queryHomePageListCountByUserId(userId, listDto);
            list = jobMapper.queryHomePageListByUserId(userId, listDto);
        }
        Page<JobHomePageListVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 根据求职id查询首页职位信息详情
     *
     * @param id
     * @return
     */
    @Override
    public JobHomePageDetailVo queryHomePageDetailById(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        JobHomePageDetailVo jobHomePageDetailVo = jobMapper.queryHomePageDetailById(userId, id);
        BigDecimal award = jobHomePageDetailVo.getAward();
        if (null != award) {
            jobHomePageDetailVo.setAward(award.multiply(new BigDecimal("0.01")));
        }
        return jobHomePageDetailVo;
    }

    /**
     * 根据职位信息中的企业id查询企业信息详情
     *
     * @param id
     * @return
     */
    @Override
    public EnterDetailVo queryEnterpriseDetailById(Long id) {
        return jobMapper.queryEnterpriseDetailById(id);
    }

    /**
     * 根据职位id发起职位申请
     *
     * @param applyDto
     */
    @Override
    public void apply(JobApplyDto applyDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Integer authenticationStatus = loginUser.getAuthenticationStatus();
        if (UserAuthenticationStatusEnum.CERTIFIED.getStatus() != authenticationStatus)
            throw new YouyaException("请先完成实名认证");
        Long jobId = applyDto.getJobId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, jobId).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Integer status = job.getStatus();
        Integer auditStatus = job.getAuditStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() != status || JobAuditStatusEnum.AUDIT_SUCCESS.getStatus() != auditStatus)
            throw new YouyaException("职位暂未发布");
        Long userId = loginUser.getId();
        boolean exists = applyJobMapper.exists(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getJobId, jobId).eq(ApplyJob::getApplicant, userId).eq(ApplyJob::getIsDelete, 0));
        if (exists) throw new YouyaException("您已申请过该职位，请勿重复申请");
        ApplyJob applyJob = new ApplyJob();
        applyJob.setJobId(jobId).setApplicant(userId);
        Long referee = applyDto.getReferee();
        if (null != referee) {
            applyJob.setReferee(referee);
        }
        applyJobMapper.insert(applyJob);
    }

    /**
     * 收藏或取消收藏职位信息
     *
     * @param id
     */
    @Override
    public void collect(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        boolean exists = attentionJobMapper.exists(new LambdaQueryWrapper<AttentionJob>().eq(AttentionJob::getUid, userId).eq(AttentionJob::getJobId, id));
        //取消收藏
        if (exists) {
            attentionJobMapper.delete(new LambdaQueryWrapper<AttentionJob>().eq(AttentionJob::getUid, userId).eq(AttentionJob::getJobId, id));
        } else {//收藏
            AttentionJob a = new AttentionJob();
            a.setUid(userId);
            a.setJobId(id);
            attentionJobMapper.insert(a);
        }
    }

    /**
     * 根据id查询分享信息
     *
     * @param id
     * @return
     */
    @Override
    public JobShareInfo queryShareInfo(Long id) {
        JobShareInfo shareInfo = jobMapper.queryShareInfo(id);
        Long userId = SpringSecurityUtil.getUserId();
        UserPublicInfoVo userPublicInfoVo = userMapper.queryUserPublicInfo(userId);
        shareInfo.setRefereeAvatar(userPublicInfoVo.getAvatar());
        shareInfo.setRefereeLastName(userPublicInfoVo.getLastName());
        shareInfo.setRefereeFirstName(userPublicInfoVo.getFirstName());
        shareInfo.setRefereeGender(userPublicInfoVo.getGender());
        BigDecimal award = shareInfo.getAward();
        if (null != award) {
            shareInfo.setAward(award.multiply(new BigDecimal("0.01")));
        }
        return shareInfo;
    }

    /**
     * 查询用户个人职位列表
     *
     * @param personalListDto
     * @return
     */
    @Override
    public Page<JobPersonalVo> queryPersonalList(JobQueryPersonalListDto personalListDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) return null;
        Long userId = loginUser.getId();
        Integer status = personalListDto.getStatus();
        int pageNumber = personalListDto.getPageNumber();
        int pageSize = personalListDto.getPageSize();
        Long count = jobMapper.selectCount(new LambdaQueryWrapper<Job>().eq(Job::getEnterpriseId, enterpriseId).eq(Job::getUid, userId).eq(Job::getStatus, status).eq(Job::getIsDelete, 0));
        List<JobPersonalVo> list = jobMapper.queryPersonalList(userId, enterpriseId, personalListDto);
        Page<JobPersonalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 创建职位信息
     *
     * @param createDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(JobCreateDto createDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("请先认证企业或者加入企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未注册");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus)
            throw new YouyaException("请等待企业通过审核再发布职位");
        String award = createDto.getAward();
        Job job = new Job();
        Long id = IdWorker.getId();
        job.setId(id);
        if (StringUtils.isNotBlank(award)) {
            if (CalculationUtil.containsNonNumericCharacter(award)) throw new YouyaException("请输入有效金额");
            BigDecimal amount = new BigDecimal(award);
            if (CalculationUtil.isNegativeNumber(amount)) throw new YouyaException("奖励金额必须是正数");
            BigDecimal divisor = new BigDecimal("1");
            boolean isPositiveInteger = amount.remainder(divisor).compareTo(BigDecimal.ZERO) == 0;
            if (!isPositiveInteger) throw new YouyaException("奖励金额必须是1的整数倍");
            BigDecimal interviewRewardRate = createDto.getInterviewRewardRate();
            if (null == interviewRewardRate) throw new YouyaException("面试奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(interviewRewardRate))
                throw new YouyaException("面试奖励分配比例必须是正数");
            int interviewRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(interviewRewardRate);
            if (interviewRewardRateScale > 2) throw new YouyaException("面试奖励分配比例最多只能包含两位有效小数位");
            BigDecimal onboardRewardRate = createDto.getOnboardRewardRate();
            if (null == onboardRewardRate) throw new YouyaException("入职奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(onboardRewardRate))
                throw new YouyaException("入职奖励分配比例必须是正数");
            int onboardRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(onboardRewardRate);
            if (onboardRewardRateScale > 2) throw new YouyaException("入职奖励分配比例最多只能包含两位有效小数位");
            BigDecimal fullMemberRewardRate = createDto.getFullMemberRewardRate();
            if (null == fullMemberRewardRate) throw new YouyaException("转正奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(fullMemberRewardRate))
                throw new YouyaException("转正奖励分配比例比例必须是正数");
            int fullMemberRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(fullMemberRewardRate);
            if (fullMemberRewardRateScale > 2) throw new YouyaException("转正奖励分配比例最多只能包含两位有效小数位");
            BigDecimal total = interviewRewardRate.add(onboardRewardRate).add(fullMemberRewardRate);
            if (total.compareTo(new BigDecimal("100")) != 0) throw new YouyaException("所有奖励比例相加必须满足100%");
            BonusDistributionRule interviewBonusDistributionRule = bonusDistributionRuleMapper.selectOne(new LambdaQueryWrapper<BonusDistributionRule>().eq(BonusDistributionRule::getCode, JOB_INTERVIEW_BONUS_DISTRIBUTION_RULE).eq(BonusDistributionRule::getIsDelete, 0));
            if (null == interviewBonusDistributionRule)
                throw new YouyaException("职位面试阶段预设奖金分配规则缺失，请联系客服");
            job.setInterviewBonusDistributionRule(JOB_INTERVIEW_BONUS_DISTRIBUTION_RULE);
            BonusDistributionRule onboardBonusDistributionRule = bonusDistributionRuleMapper.selectOne(new LambdaQueryWrapper<BonusDistributionRule>().eq(BonusDistributionRule::getCode, JOB_ONBOARD_BONUS_DISTRIBUTION_RULE).eq(BonusDistributionRule::getIsDelete, 0));
            if (null == onboardBonusDistributionRule)
                throw new YouyaException("职位入职阶段预设奖金分配规则缺失，请联系客服");
            job.setOnboardBonusDistributionRule(JOB_ONBOARD_BONUS_DISTRIBUTION_RULE);
            BonusDistributionRule fullMemberBonusDistributionRule = bonusDistributionRuleMapper.selectOne(new LambdaQueryWrapper<BonusDistributionRule>().eq(BonusDistributionRule::getCode, JOB_FULL_MEMBER_BONUS_DISTRIBUTION_RULE).eq(BonusDistributionRule::getIsDelete, 0));
            if (null == fullMemberBonusDistributionRule)
                throw new YouyaException("职位面试阶段预设奖金分配规则缺失，请联系客服");
            job.setFullMemberBonusDistributionRule(JOB_FULL_MEMBER_BONUS_DISTRIBUTION_RULE);
            BigDecimal totalAward = amount.multiply(new BigDecimal("100"));
            job.setAward(totalAward);
        }
        String address = districtDataMapper.searchAddressByCode(createDto.getProvinceCode(), createDto.getCityCode());
        String detailAddress = address + createDto.getDetailAddress();
        Location location = TencentMapUtil.geocode(detailAddress);
        if (null == location) throw new YouyaException("地址解析失败，请重新填写地址信息");
        Long userId = loginUser.getId();
        BeanUtils.copyProperties(createDto, job);
        job.setEnterpriseId(enterpriseId);
        job.setUid(userId);
        job.setStatus(JobStatusEnum.PUBLISHED.getStatus());
        job.setAuditStatus(JobAuditStatusEnum.UNAUDITED.getStatus());
        job.setNewStatus(JobNewStatusEnum.PENDING_APPROVAL);
        job.setCountryCode(CHINA_CODE);
        job.setLongitude(location.getLng());
        job.setLatitude(location.getLat());
        jobMapper.insert(job);
        List<Long> welfareLabelIdList = createDto.getWelfareLabelIdList();
        if (welfareLabelIdList.size() > 0) {
            List<JobWelfareLabel> list = new ArrayList<>();
            welfareLabelIdList.forEach(s -> {
                JobWelfareLabel jobWelfareLabel = new JobWelfareLabel();
                jobWelfareLabel.setId(IdWorker.getId()).setJobId(job.getId()).setLabelId(s).setCreateTime(LocalDateTime.now()).setIsDelete(0);
                list.add(jobWelfareLabel);
            });
            jobWelfareLabelMapper.batchInsert(list);
        }
    }

    /**
     * 修改职位信息
     *
     * @param modifyDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(JobModifyDto modifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Long id = modifyDto.getId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, id).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位不存在");
        if (!userId.equals(job.getUid())) throw new YouyaException("只有发布人才可以修改职位信息");
        Integer status = job.getStatus();
//        if (JobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可修改");
//        Integer auditStatus = job.getAuditStatus();
//        if (JobAuditStatusEnum.UNAUDITED.getStatus() == auditStatus)
//            throw new YouyaException("等待审核中的职位不可修改");
        if (job.getNewStatus() == JobNewStatusEnum.PENDING_APPROVAL) {
            throw new YouyaException("等待审核中的职位不可修改");
        }
        if (job.getNewStatus() == JobNewStatusEnum.APPROVED) {
            throw new YouyaException("已发布的职位不可修改");
        }
        String award = modifyDto.getAward();
        if (StringUtils.isNotBlank(award)) {
            if (CalculationUtil.containsNonNumericCharacter(award)) throw new YouyaException("请输入有效金额");
            BigDecimal amount = new BigDecimal(award);
            if (CalculationUtil.isNegativeNumber(amount)) throw new YouyaException("奖励金额必须是正数");
            BigDecimal divisor = new BigDecimal("1");
            boolean isPositiveInteger = amount.remainder(divisor).compareTo(BigDecimal.ZERO) == 0;
            if (!isPositiveInteger) throw new YouyaException("奖励金额必须是1的整数倍");
            BigDecimal interviewRewardRate = modifyDto.getInterviewRewardRate();
            if (null == interviewRewardRate) throw new YouyaException("面试奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(interviewRewardRate))
                throw new YouyaException("面试奖励分配比例必须是正数");
            int interviewRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(interviewRewardRate);
            if (interviewRewardRateScale > 2) throw new YouyaException("面试奖励分配比例最多只能包含两位有效小数位");
            BigDecimal onboardRewardRate = modifyDto.getOnboardRewardRate();
            if (null == onboardRewardRate) throw new YouyaException("入职奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(onboardRewardRate))
                throw new YouyaException("入职奖励分配比例必须是正数");
            int onboardRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(onboardRewardRate);
            if (onboardRewardRateScale > 2) throw new YouyaException("入职奖励分配比例最多只能包含两位有效小数位");
            BigDecimal fullMemberRewardRate = modifyDto.getFullMemberRewardRate();
            if (null == fullMemberRewardRate) throw new YouyaException("转正奖励分配比例不能为空");
            if (CalculationUtil.isNegativeNumber(fullMemberRewardRate))
                throw new YouyaException("转正奖励分配比例比例必须是正数");
            int fullMemberRewardRateScale = CalculationUtil.getScaleAfterStrippingTrailingZeros(fullMemberRewardRate);
            if (fullMemberRewardRateScale > 2) throw new YouyaException("转正奖励分配比例最多只能包含两位有效小数位");
            BigDecimal total = interviewRewardRate.add(onboardRewardRate).add(fullMemberRewardRate);
            if (total.compareTo(new BigDecimal("100")) != 0) throw new YouyaException("所有奖励比例相加必须满足100%");
            BigDecimal totalAward = amount.multiply(new BigDecimal("100"));
            job.setAward(totalAward);
        }
        String oldAddress = districtDataMapper.searchAddressByCode(job.getProvinceCode(), job.getCityCode());
        String oldDetailAddress = oldAddress + job.getDetailAddress();
        String newAddress = districtDataMapper.searchAddressByCode(modifyDto.getProvinceCode(), modifyDto.getCityCode());
        String newDetailAddress = newAddress + modifyDto.getDetailAddress();
        if (!oldDetailAddress.equals(newDetailAddress)) {
            Location location = TencentMapUtil.geocode(newDetailAddress);
            if (null == location) throw new YouyaException("地址解析失败，请重新填写地址信息");
            modifyDto.setLongitude(location.getLng());
            modifyDto.setLatitude(location.getLat());
        }
        jobMapper.modify(modifyDto);
        List<Long> oldWelfareLabelIdList = jobWelfareLabelMapper.selectWelfareLabelIdListByJobId(job.getId());
        List<Long> newWelfareLabelIdList = modifyDto.getWelfareLabelIdList();

        //找出新增的福利标签
        List<Long> addedIdList = newWelfareLabelIdList.stream()
                .filter(e -> !oldWelfareLabelIdList.contains(e))
                .collect(Collectors.toList());
        if (addedIdList.size() > 0) {
            List<JobWelfareLabel> list = new ArrayList<>();
            addedIdList.forEach(s -> {
                JobWelfareLabel jobWelfareLabel = new JobWelfareLabel();
                jobWelfareLabel.setId(IdWorker.getId()).setJobId(job.getId()).setLabelId(s).setCreateTime(LocalDateTime.now()).setIsDelete(0);
                list.add(jobWelfareLabel);
            });
            jobWelfareLabelMapper.batchInsert(list);
        }

        //找出删除的福利标签
        List<Long> removedIdList = oldWelfareLabelIdList.stream()
                .filter(e -> !newWelfareLabelIdList.contains(e))
                .collect(Collectors.toList());
        if (removedIdList.size() > 0) {
            jobWelfareLabelMapper.batchModify(job.getId(), removedIdList);
        }
    }

    /**
     * 根据id查询职位信息详情
     *
     * @param id
     * @return
     */
    @Override
    public JobDetailVo detail(Long id) {
        JobDetailVo detail = jobMapper.detail(id);
        BigDecimal award = detail.getAward();
        if (null != award) {
            detail.setAward(award.multiply(new BigDecimal("0.01")));
        }
        return detail;
    }

    /**
     * 根据id发布职位
     *
     * @param id
     */
    @Override
    public void publish(Long id) {
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, id).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
//        Integer status = job.getStatus();
//        if (JobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("职位已发布");
        if (job.getNewStatus() == JobNewStatusEnum.PENDING_APPROVAL) {
            throw new YouyaException("职位审核中");
        }
        if (job.getNewStatus() == JobNewStatusEnum.APPROVED) {
            throw new YouyaException("职位已发布");
        }
        job.setStatus(JobStatusEnum.PUBLISHED.getStatus());
        job.setAuditStatus(JobAuditStatusEnum.UNAUDITED.getStatus());
        job.setNewStatus(JobNewStatusEnum.PENDING_APPROVAL);
        jobMapper.updateById(job);
    }

    /**
     * 根据id关闭职位
     *
     * @param id
     */
    @Override
    public void close(Long id) {
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, id).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long userId = loginUser.getId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
//        Integer status = job.getStatus();
//        if (JobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("职位已关闭");
//        Integer auditStatus = job.getAuditStatus();
//        if (JobAuditStatusEnum.UNAUDITED.getStatus() == auditStatus) throw new YouyaException("审核中的职位无法关闭");

        if (job.getNewStatus() == JobNewStatusEnum.PENDING_APPROVAL) {
            throw new YouyaException("审核中的职位无法关闭");
        }
        if (job.getNewStatus() == JobNewStatusEnum.CLOSED) {
            throw new YouyaException("职位已关闭");
        }

        job.setStatus(JobStatusEnum.UNPUBLISHED.getStatus());
        job.setNewStatus(JobNewStatusEnum.CLOSED);
        jobMapper.updateById(job);
    }

    /**
     * 根据id删除职位
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, id).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位信息不存在");
        Long uid = job.getUid();
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Integer status = job.getStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("发布中的职位不可删除");
        job.setIsDelete(1);
        jobMapper.updateById(job);
    }
}
