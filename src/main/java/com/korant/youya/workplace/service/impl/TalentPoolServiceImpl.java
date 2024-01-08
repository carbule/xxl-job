package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.enums.job.JobAuditStatusEnum;
import com.korant.youya.workplace.enums.job.JobStatusEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.InternalRecommendMapper;
import com.korant.youya.workplace.mapper.JobMapper;
import com.korant.youya.workplace.mapper.TalentPoolMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.talentpool.AssociateDto;
import com.korant.youya.workplace.pojo.dto.talentpool.QueryPublishedJobListDto;
import com.korant.youya.workplace.pojo.dto.talentpool.TalentPoolQueryListDto;
import com.korant.youya.workplace.pojo.po.InternalRecommend;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.pojo.vo.talentpool.PublishedJobVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentDetailVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentPoolVo;
import com.korant.youya.workplace.pojo.vo.talentpool.TalentRecruitmentRecordsVo;
import com.korant.youya.workplace.service.TalentPoolService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
    private JobMapper jobMapper;

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
}
