package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EnterpriseMapper;
import com.korant.youya.workplace.mapper.JobMapper;
import com.korant.youya.workplace.pojo.Location;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.job.JobCreateDto;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.job.JobDetailVo;
import com.korant.youya.workplace.service.JobService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.korant.youya.workplace.utils.TencentMapUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Resource
    private JobMapper jobMapper;

    @Resource
    private EnterpriseMapper enterpriseMapper;

    private static final String CHINA_CODE = "100000";

    /**
     * 创建职位信息
     *
     * @param createDto
     */
    @Override
    public void create(JobCreateDto createDto) {
        LoginUser loginUser = SpringSecurityUtil.getUserInfo();
        Long enterpriseId = loginUser.getEnterpriseId();
        if (null == enterpriseId) throw new YouyaException("请先认证企业或者加入企业");
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getId, enterpriseId).eq(Enterprise::getIsDelete, 0));
        if (null == enterprise) throw new YouyaException("企业未注册");
        Integer authStatus = enterprise.getAuthStatus();
        if (EnterpriseAuthStatusEnum.AUTH_SUCCESS.getStatus() != authStatus) throw new YouyaException("请等待企业通过审核再发布职位");
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
        String address = createDto.getProvinceName() + createDto.getCityName() + createDto.getDistrictName();
        Location location = TencentMapUtil.geocode(address);
        if (null == location) throw new YouyaException("地址解析失败，请重新填写地址信息");
        Long userId = loginUser.getId();
        Job job = new Job();
        BeanUtils.copyProperties(createDto, job);
        job.setEnterpriseId(enterpriseId);
        job.setUid(userId);
        job.setStatus(JobStatusEnum.UNPUBLISHED.getStatus());
        job.setAuditStatus(JobAuditStatusEnum.UNAUDITED.getStatus());
        job.setCountryCode(CHINA_CODE);
        job.setLongitude(location.getLng());
        job.setLatitude(location.getLat());
        jobMapper.insert(job);
    }

    /**
     * 修改职位信息
     *
     * @param modifyDto
     */
    @Override
    public void modify(JobModifyDto modifyDto) {
        Long userId = SpringSecurityUtil.getUserId();
        Long id = modifyDto.getId();
        Job job = jobMapper.selectOne(new LambdaQueryWrapper<Job>().eq(Job::getId, id).eq(Job::getIsDelete, 0));
        if (null == job) throw new YouyaException("职位不存在");
        if (!userId.equals(job.getUid())) throw new YouyaException("只有发布人才可以修改职位信息");
        Integer status = job.getStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("已发布的职位不可修改");
        Integer auditStatus = job.getAuditStatus();
        if (JobAuditStatusEnum.UNAUDITED.getStatus() == auditStatus) throw new YouyaException("等待审核中的职位不可修改");
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
        //todo 缺少腾讯地图经纬度
        jobMapper.modify(modifyDto);
    }

    /**
     * 根据id查询职位信息详情
     *
     * @param id
     * @return
     */
    @Override
    public JobDetailVo detail(Long id) {
        return null;
    }

    /**
     * 根据id删除职位
     *
     * @param id
     */
    @Override
    public void delete(Long id) {

    }
}
