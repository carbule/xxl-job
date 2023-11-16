package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.JobIntentionMapper;
import com.korant.youya.workplace.pojo.SessionLocal;
import com.korant.youya.workplace.pojo.dto.jobintention.JobIntentionModifyDto;
import com.korant.youya.workplace.pojo.po.JobIntention;
import com.korant.youya.workplace.pojo.vo.jobIntention.JobIntentionVo;
import com.korant.youya.workplace.service.JobIntentionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 求职意向 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-15
 */
@Service
public class JobIntentionServiceImpl extends ServiceImpl<JobIntentionMapper, JobIntention> implements JobIntentionService {

    @Resource
    private JobIntentionMapper jobIntentionMapper;

    /**
     * 查询求职状态
     *
     * @param
     **/
    @Override
    public JobIntentionVo status() {

        Long userId = SessionLocal.getUserId();
        return jobIntentionMapper.status(userId);

    }

    /**
     * 修改求职状态
     *
     * @param jobIntentionModifyDto
     */
    @Override
    public void modify(JobIntentionModifyDto jobIntentionModifyDto) {

        Long userId = SessionLocal.getUserId();
        jobIntentionMapper.update(new JobIntention(),
                new LambdaUpdateWrapper<JobIntention>()
                        .eq(JobIntention::getUid, userId)
                        .set(JobIntention::getStatus, jobIntentionModifyDto.getStatus()));

    }

}
