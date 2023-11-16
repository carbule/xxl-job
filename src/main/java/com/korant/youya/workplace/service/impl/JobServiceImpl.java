package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.JobMapper;
import com.korant.youya.workplace.pojo.po.Job;
import com.korant.youya.workplace.service.JobService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

}
