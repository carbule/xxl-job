package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.ConfirmationMapper;
import com.korant.youya.workplace.pojo.dto.confirmation.ConfirmationQueryListDto;
import com.korant.youya.workplace.pojo.po.Confirmation;
import com.korant.youya.workplace.pojo.vo.confirmation.ConfirmationVo;
import com.korant.youya.workplace.service.ConfirmationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 转正记录表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-29
 */
@Service
public class ConfirmationServiceImpl extends ServiceImpl<ConfirmationMapper, Confirmation> implements ConfirmationService {

    @Resource
    private ConfirmationMapper confirmationMapper;

    /**
     * 查询转正邀请列表
     *
     * @param listDto
     * @return
     */
    @Override
    public Page<ConfirmationVo> queryList(ConfirmationQueryListDto listDto) {
        Long recruitProcessInstanceId = listDto.getRecruitProcessInstanceId();
        int pageNumber = listDto.getPageNumber();
        int pageSize = listDto.getPageSize();
        Long count = confirmationMapper.selectCount(new LambdaQueryWrapper<Confirmation>().eq(Confirmation::getRecruitProcessInstanceId, recruitProcessInstanceId).eq(Confirmation::getIsDelete, 0));
        List<ConfirmationVo> list = confirmationMapper.queryList(listDto);
        Page<ConfirmationVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;
    }
}
