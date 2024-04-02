package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.HuntJobMainTask;

/**
 * <p>
 * 企业招聘任务主表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
public interface HuntJobMainTaskService extends IService<HuntJobMainTask> {

    /**
     * 查询子任务执行状态
     *
     * @param msg
     */
    void querySubTaskStatus(String msg);
}
