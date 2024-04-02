package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.JobBonusAllocation;
import com.korant.youya.workplace.pojo.po.JobMainTask;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
public interface JobMainTaskService extends IService<JobMainTask> {

    /**
     * 职位奖金分配
     *
     * @param bonusAllocation
     */
    void bonusAllocation(JobBonusAllocation bonusAllocation);

    /**
     * 查询子任务执行状态
     *
     * @param mainTaskId
     */
    void querySubTaskStatus(Long mainTaskId);
}
