package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.jobintention.JobIntentionModifyDto;
import com.korant.youya.workplace.pojo.po.JobIntention;
import com.korant.youya.workplace.pojo.vo.jobIntention.JobIntentionVo;

/**
 * <p>
 * 求职意向 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-15
 */
public interface JobIntentionService extends IService<JobIntention> {

    JobIntentionVo status();

    void modify(JobIntentionModifyDto jobIntentionModifyDto);

}
