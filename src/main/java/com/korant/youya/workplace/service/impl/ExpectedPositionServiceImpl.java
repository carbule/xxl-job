package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.ExpectedPositionMapper;
import com.korant.youya.workplace.mapper.HuntJobMapper;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.service.ExpectedPositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户意向职位表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@Service
public class ExpectedPositionServiceImpl extends ServiceImpl<ExpectedPositionMapper, ExpectedPosition> implements ExpectedPositionService {

    @Resource
    private HuntJobMapper huntJobMapper;

    /**
     * 删除意向职位前校验
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkBeforeDelete(Long id) {
        return huntJobMapper.exists(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getPositionId, id).eq(HuntJob::getIsDelete, 0));
    }
}
