package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.ApplyJobMapper;
import com.korant.youya.workplace.pojo.dto.applyjob.ApplyJobQueryListDto;
import com.korant.youya.workplace.pojo.po.ApplyJob;
import com.korant.youya.workplace.pojo.vo.applyjob.ApplyJobVo;
import com.korant.youya.workplace.service.ApplyJobService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 职位申请表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class ApplyJobServiceImpl extends ServiceImpl<ApplyJobMapper, ApplyJob> implements ApplyJobService {

    @Resource
    private ApplyJobMapper applyJobMapper;

    /**
     * 查询用户已申请职位列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ApplyJobVo> queryList(ApplyJobQueryListDto listDto) {
        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = applyJobMapper.selectCount(new LambdaQueryWrapper<ApplyJob>().eq(ApplyJob::getApplicant, userId).eq(ApplyJob::getIsDelete, 0));
        List<ApplyJobVo> list = applyJobMapper.queryList(userId, listDto);
        Page<ApplyJobVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }
}
