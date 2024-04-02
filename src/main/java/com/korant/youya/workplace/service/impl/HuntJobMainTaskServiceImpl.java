package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.HuntJobMainTaskMapper;
import com.korant.youya.workplace.pojo.po.HuntJobMainTask;
import com.korant.youya.workplace.service.HuntJobMainTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业招聘任务主表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2024-03-29
 */
@Service
public class HuntJobMainTaskServiceImpl extends ServiceImpl<HuntJobMainTaskMapper, HuntJobMainTask> implements HuntJobMainTaskService {

    /**
     * 查询子任务执行状态
     *
     * @param msg
     */
    @Override
    public void querySubTaskStatus(String msg) {

    }
}
