package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.pojo.vo.interview.InterviewVo;

/**
 * <p>
 * 面试记录表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
public interface InterviewService extends IService<Interview> {

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    Page<InterviewVo> queryList(InterviewQueryListDto listDto);
}
