package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.InterviewMapper;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.service.InterviewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 面试记录表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements InterviewService {

}
