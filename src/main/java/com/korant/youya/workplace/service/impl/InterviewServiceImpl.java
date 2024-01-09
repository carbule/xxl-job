package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.InterviewMapper;
import com.korant.youya.workplace.pojo.dto.interview.InterviewQueryListDto;
import com.korant.youya.workplace.pojo.po.Interview;
import com.korant.youya.workplace.pojo.vo.interview.InterviewVo;
import com.korant.youya.workplace.service.InterviewService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Resource
    private InterviewMapper interviewMapper;

    /**
     * 查询面试邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<InterviewVo> queryList(InterviewQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = interviewMapper.selectCount(new LambdaQueryWrapper<Interview>().eq(Interview::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Interview::getIsDelete, 0));
        List<InterviewVo> list = interviewMapper.queryList(listDto);
        Page<InterviewVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }
}
