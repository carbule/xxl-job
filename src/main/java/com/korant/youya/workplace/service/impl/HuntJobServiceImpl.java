package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.huntjob.HuntJobStatusEnum;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.enums.user.UserAuthenticationStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobCreateDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobModifyDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.dto.huntjob.HuntJobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionHuntJob;
import com.korant.youya.workplace.pojo.po.EducationExperience;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.pojo.po.User;
import com.korant.youya.workplace.pojo.vo.expectedposition.PersonalExpectedPositionVo;
import com.korant.youya.workplace.pojo.vo.expectedworkarea.PersonalExpectedWorkAreaVo;
import com.korant.youya.workplace.pojo.vo.huntjob.*;
import com.korant.youya.workplace.service.HuntJobService;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Resource
    private ExpectedPositionMapper expectedPositionMapper;

    @Resource
    private ExpectedWorkAreaMapper expectedWorkAreaMapper;

    private static final String NANJING_CITY_CODE = "320100";

    /**
     * 查询首页求职信息列表
     *
     * @param listDto
     * @param request
     * @return
     */
    @Override
    public Page<HuntJobHomePageVo> queryHomePageList(HuntJobQueryHomePageListDto listDto, HttpServletRequest request) {
        String token = request.getHeader("token");
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        int count;
        List<HuntJobHomePageVo> list;
        if (StringUtils.isBlank(token)) {
            listDto.setCityCode(NANJING_CITY_CODE);
            count = huntJobMapper.queryHomePageListCount(listDto);
            list = huntJobMapper.queryHomePageList(listDto);
        } else {
            Long userId = null;
            try {
                userId = JwtUtil.getId(token);
            } catch (Exception e) {
                throw new YouyaException("token校验失败");
            }
            LoginUser loginUser = userMapper.selectUserToLoginById(userId);
            if (null == loginUser) return null;
            Long enterpriseId = loginUser.getEnterpriseId();
            count = huntJobMapper.queryHomePageListCountByUserId(userId, enterpriseId, listDto);
            list = huntJobMapper.queryHomePageListByUserId(userId, enterpriseId, listDto);
        }
        Page<HuntJobHomePageVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }

    /**
     * 根据求职id查询首页求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobHomePageDetailVo queryHomePageDetailById(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.queryHomePageDetailById(userId, id);
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
     * 查询用户个人求职列表
     *
     * @param personalListDto
     * @return
     */
    @Override
    public Page<HuntJobPersonalVo> queryPersonalList(HuntJobQueryPersonalListDto personalListDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Integer status = personalListDto.getStatus();
        int pageNumber = personalListDto.getPageNumber();
        int pageSize = personalListDto.getPageSize();
        Long count = huntJobMapper.selectCount(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getUid, userId).eq(HuntJob::getStatus, status).eq(HuntJob::getIsDelete, 0));
        List<HuntJobPersonalVo> list = huntJobMapper.queryPersonalList(userId, status, personalListDto);
        Page<HuntJobPersonalVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
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
        Integer gender = user.getGender();
        if (null == gender) throw new YouyaException("请完善个人信息中性别");
        LocalDate birthday = user.getBirthday();
        if (null == birthday) throw new YouyaException("请完善个人信息中出生日期");
        boolean exists = educationExperienceMapper.exists(new LambdaQueryWrapper<EducationExperience>().eq(EducationExperience::getUid, userId).eq(EducationExperience::getIsDelete, 0));
        if (!exists) throw new YouyaException("请至少补充一条教育经历");
        LocalDate startWorkingTime = user.getStartWorkingTime();
        if (null == startWorkingTime) throw new YouyaException("请完善个人信息中开始工作时间");
        int positionCount = expectedPositionMapper.selectCountByUserId(userId);
        if (positionCount <= 0) throw new YouyaException("请至少添加一条意向职位");
        int workAreaCount = expectedWorkAreaMapper.selectCountByUserId(userId);
        if (workAreaCount <= 0) throw new YouyaException("请至少添加一条意向区域");
    }

    /**
     * 查询个人意向职位列表
     *
     * @return
     */
    @Override
    public List<PersonalExpectedPositionVo> queryPersonalExpectedPositionList() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.queryPersonalExpectedPositionList(userId);
    }

    /**
     * 查询个人意向区域列表
     *
     * @return
     */
    @Override
    public List<PersonalExpectedWorkAreaVo> queryPersonalExpectedWorkAreaList() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.queryPersonalExpectedWorkAreaList(userId);
    }

    /**
     * 求职发布预览
     *
     * @return
     */
    @Override
    public HuntJobPublishPreviewVo publishPreview() {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.publishPreview(userId);
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
        if (HuntJobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可修改");
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
     * 根据id预览求职详细信息
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailsPreviewVo detailsPreview(Long id) {
        Long userId = SpringSecurityUtil.getUserId();
        return huntJobMapper.detailsPreview(userId, id);
    }

    /**
     * 根据id查询求职信息详情
     *
     * @param id
     * @return
     */
    @Override
    public HuntJobDetailVo detail(Long id) {
        return huntJobMapper.detail(id);
    }

    /**
     * 根据id关闭职位
     *
     * @param id
     */
    @Override
    public void close(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Long userId = SpringSecurityUtil.getUserId();
        if (!userId.equals(huntJob.getUid())) throw new YouyaException("非法操作");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("当前求职信息已关闭");
        huntJob.setStatus(HuntJobStatusEnum.UNPUBLISHED.getStatus());
        huntJobMapper.updateById(huntJob);
    }

    /**
     * 根据id发布职位
     *
     * @param id
     */
    @Override
    public void release(Long id) {
        HuntJob huntJob = huntJobMapper.selectOne(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getId, id).eq(HuntJob::getIsDelete, 0));
        if (null == huntJob) throw new YouyaException("求职信息不存在");
        Long userId = SpringSecurityUtil.getUserId();
        if (!userId.equals(huntJob.getUid())) throw new YouyaException("非法操作");
        Integer status = huntJob.getStatus();
        if (HuntJobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("当前求职信息已发布");
        huntJob.setStatus(HuntJobStatusEnum.PUBLISHED.getStatus());
        huntJobMapper.updateById(huntJob);
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
