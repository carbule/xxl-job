package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.ExpectedWorkAreaMapper;
import com.korant.youya.workplace.mapper.HuntJobMapper;
import com.korant.youya.workplace.pojo.po.ExpectedWorkArea;
import com.korant.youya.workplace.pojo.po.HuntJob;
import com.korant.youya.workplace.service.ExpectedWorkAreaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户期望工作区域表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class ExpectedWorkAreaServiceImpl extends ServiceImpl<ExpectedWorkAreaMapper, ExpectedWorkArea> implements ExpectedWorkAreaService {

    @Resource
    private HuntJobMapper huntJobMapper;

    /**
     * 删除意向区域前校验
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkBeforeDelete(Long id) {
        return huntJobMapper.exists(new LambdaQueryWrapper<HuntJob>().eq(HuntJob::getAreaId, id).eq(HuntJob::getIsDelete, 0));
    }
}
