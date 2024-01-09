package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.OnboardingMapper;
import com.korant.youya.workplace.pojo.dto.onboarding.OnboardingQueryListDto;
import com.korant.youya.workplace.pojo.po.Onboarding;
import com.korant.youya.workplace.pojo.vo.onboarding.OnboardingVo;
import com.korant.youya.workplace.service.OnboardingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 入职记录表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class OnboardingServiceImpl extends ServiceImpl<OnboardingMapper, Onboarding> implements OnboardingService {

    @Resource
    private OnboardingMapper onboardingMapper;

    /**
     * 查询入职邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<OnboardingVo> queryList(OnboardingQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = onboardingMapper.selectCount(new LambdaQueryWrapper<Onboarding>().eq(Onboarding::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Onboarding::getIsDelete, 0));
        List<OnboardingVo> list = onboardingMapper.queryList(listDto);
        Page<OnboardingVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }
}
