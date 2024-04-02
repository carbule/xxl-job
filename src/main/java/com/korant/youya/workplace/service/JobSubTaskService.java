package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.JobSubTask;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
public interface JobSubTaskService extends IService<JobSubTask> {

    /**
     * 职位奖金支付
     *
     * @param subTaskId
     */
    void bonusPayment(Long subTaskId);
}
