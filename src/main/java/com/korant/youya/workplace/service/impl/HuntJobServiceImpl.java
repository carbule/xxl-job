package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.huntjob.HuntJobAuditStatusEnum;
import com.korant.youya.workplace.enums.huntjob.HuntJobStatusEnum;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.AttentionHuntJobMapper;
import com.korant.youya.workplace.mapper.EducationExperienceMapper;
import com.korant.youya.workplace.mapper.HuntJobMapper;
import com.korant.youya.workplace.mapper.UserMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryListDto;
import com.korant.youya.workplace.pojo.po.AttentionHuntJob;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobDetailVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobListOnHomePageVo;
import com.korant.youya.workplace.pojo.vo.huntjob.HuntJobPreviewVo;
import com.korant.youya.workplace.service.HuntJobService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 求职表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class HuntJobServiceImpl extends ServiceImpl<HuntJobMapper, HuntJob> implements HuntJobService {

    @Resource
    private HuntJobMapper huntJobMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EducationExperienceMapper educationExperienceMapper;

    @Resource
    private AttentionHuntJobMapper attentionHuntJobMapper;

    /**
     * 查询首页求职信息列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<HuntJobListOnHomePageVo> queryListOnHomePage(HuntJobQueryListDto listDto) {
        return null;
    }

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailOnHomePageVo queryDetailOnHomePageById(Long id) {
        return null;
    }

    /**
     * 收藏或取消收藏求职信息
     *
     * @param id
     */
    @Override
    public void collect(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        boolean exists = attentionHuntJobMapper.exists(new LambdaQueryWrapper<AttentionHuntJob>().eq(AttentionHuntJob::getUid, userId).eq(AttentionHuntJob::getHuntId, id));
        //取消收藏
        if (exists) {
            attentionHuntJobMapper.delete(new LambdaQueryWrapper<AttentionHuntJob>().eq(AttentionHuntJob::getUid, userId).eq(AttentionHuntJob::getHuntId, id));
        } else {//收藏
            AttentionHuntJob a = new AttentionHuntJob();
            a.setUid(userId);
            a.setHuntId(id);
            attentionHuntJobMapper.insert(a);
        }
    }

    /**
     * 校验用户信息
     */
    @Override
    public void checkUserInfo() {
        Long userId = SpringSecurityUtil.getUserId();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId).eq(User::getIsDelete, 0));
        if (null == user) throw new YouyaException("用户未注册或已注销");
        Integer authenticationStatus = user.getAuthenticationStatus();
        if (UserAuthenticationStatusEnum.NOT_CERTIFIED.getStatus() == authenticationStatus)
            throw new YouyaException("请先完成实名认证");
        Integer accountStatus = user.getAccountStatus();
        if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) throw new YouyaException("账号已被冻结,详情请咨询客服");
        LocalDate birthday = user.getBirthday();
        if (null == birthday) throw new YouyaException("请完善个人信息中出生日期");
        boolean exists = educationExperienceMapper.exists(new LambdaQueryWrapper<EducationExperience>().eq(EducationExperience::getUid, userId).eq(EducationExperience::getIsDelete, 0));
        if (!exists) throw new YouyaException("请至少补充一条教育经历");
        LocalDate startWorkingTime = user.getStartWorkingTime();
        if (null == startWorkingTime) throw new YouyaException("请完善个人信息中开始工作时间");
    }

    /**
     * 求职预览
     *
     * @return
     */
    @Override
    public HuntJobPreviewVo preview() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.preview(userId);
    }

    /**
     * 创建求职信息
     *
     * @param createDto
     */
    @Override
    public void create(HuntJobCreateDto createDto) {
        LoginUser userInfo = SpringSecurityUtil.getUserInfo();
        Integer authenticationStatus = userInfo.getAuthenticationStatus();
        if (UserAuthenticationStatusEnum.CERTIFIED.getStatus() != authenticationStatus)
            throw new YouyaException("请先完成实名认证再发布求职信息");
        Integer award = createDto.getAward();
        if (null != award) {
            Integer interviewRewardRate = createDto.getInterviewRewardRate();
            if (null == interviewRewardRate) throw new YouyaException("面试奖励分配比例不能为空");
            Integer onboardRewardRate = createDto.getOnboardRewardRate();
            if (null == onboardRewardRate) throw new YouyaException("入职奖励分配比例不能为空");
            Integer fullMemberRewardRate = createDto.getFullMemberRewardRate();
            if (null == fullMemberRewardRate) throw new YouyaException("转正奖励分配比例不能为空");
            int totalAllocationRatio = interviewRewardRate + onboardRewardRate + fullMemberRewardRate;
            if (100 != totalAllocationRatio) throw new YouyaException("面试奖励比例加入职奖励比例必须满足100%");
        }
        HuntJob huntJob = new HuntJob();
        BeanUtils.copyProperties(createDto, huntJob);
        huntJob.setUid(userInfo.getId());
        huntJob.setStatus(HuntJobStatusEnum.PUBLISHED.getStatus());
        huntJob.setAuditStatus(HuntJobAuditStatusEnum.UNAUDITED.getStatus());
        huntJob.setRefreshTime(LocalDateTime.now());
        huntJob.setRefreshTime(LocalDateTime.now());
        huntJobMapper.insert(huntJob);
    }

    /**
     * 修改求职信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(HuntJobModifyDto modifyDto) {
        Long id = modifyDto.getId();
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可修改");
        Integer award = modifyDto.getAward();
        if (null != award) {
            Integer interviewRewardRate = modifyDto.getInterviewRewardRate();
            if (null == interviewRewardRate) throw new YouyaException("面试奖励分配比例不能为空");
            Integer onboardRewardRate = modifyDto.getOnboardRewardRate();
            if (null == onboardRewardRate) throw new YouyaException("入职奖励分配比例不能为空");
            Integer fullMemberRewardRate = modifyDto.getFullMemberRewardRate();
            if (null == fullMemberRewardRate) throw new YouyaException("转正奖励分配比例不能为空");
            int totalAllocationRatio = interviewRewardRate + onboardRewardRate + fullMemberRewardRate;
            if (100 != totalAllocationRatio) throw new YouyaException("面试奖励比例加入职奖励比例必须满足100%");
        }
        huntJobMapper.modify(modifyDto);
    }

    /**
     * 查询求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailVo detail(Long id) {
        return null;
    }

    /**
     * 删除求职信息
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可删除");
        huntJob.setIsDelete(1);
        huntJobMapper.updateById(huntJob);
    }
}
