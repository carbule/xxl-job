package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.enterprise.EnterpriseAuthStatusEnum;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.*;
import com.korant.youya.workplace.pojo.Location;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.job.JobCreateDto;
import com.korant.youya.workplace.pojo.dto.job.JobModifyDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryHomePageListDto;
import com.korant.youya.workplace.pojo.dto.job.JobQueryPersonalListDto;
import com.korant.youya.workplace.pojo.po.AttentionJob;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.po.JobWelfareLabel;
import com.korant.youya.workplace.pojo.vo.job.*;
import com.korant.youya.workplace.service.JobService;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import com.korant.youya.workplace.utils.TencentMapUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DistrictDataMapper districtDataMapper;

    private static final String CHINA_CODE = "100000";

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
        return jobMapper.queryHomePageDetailById(userId, id);
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
        String address = districtDataMapper.searchAddressByCode(createDto.getProvinceCode(), createDto.getCityCode());
        String detailAddress = address + createDto.getDetailAddress();
        Location location = TencentMapUtil.geocode(detailAddress);
        if (null == location) throw new YouyaException("地址解析失败，请重新填写地址信息");
        Long userId = loginUser.getId();
        Job job = new Job();
        BeanUtils.copyProperties(createDto, job);
        job.setEnterpriseId(enterpriseId);
        job.setUid(userId);
        job.setStatus(JobStatusEnum.PUBLISHED.getStatus());
        job.setAuditStatus(JobAuditStatusEnum.UNAUDITED.getStatus());
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
        return jobMapper.detail(id);
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
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Integer status = job.getStatus();
        if (JobStatusEnum.PUBLISHED.getStatus() == status) throw new YouyaException("职位已发布");
        job.setStatus(JobStatusEnum.PUBLISHED.getStatus());
        job.setAuditStatus(JobAuditStatusEnum.UNAUDITED.getStatus());
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
        Long userId = SpringSecurityUtil.getUserId();
        if (!uid.equals(userId)) throw new YouyaException("非法操作");
        Integer status = job.getStatus();
        if (JobStatusEnum.UNPUBLISHED.getStatus() == status) throw new YouyaException("职位已关闭");
        Integer auditStatus = job.getAuditStatus();
        if (JobAuditStatusEnum.UNAUDITED.getStatus() == auditStatus) throw new YouyaException("审核中的职位无法关闭");
        job.setStatus(JobStatusEnum.UNPUBLISHED.getStatus());
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
