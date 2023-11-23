package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.ExpectedPositionMapper;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionQueryDto;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoByPositionCodeVo;
import com.korant.youya.workplace.service.ExpectedPositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private ExpectedPositionMapper expectedPositionMapper;

    /**
     * 根据职位查看求职者
     *
     * @param
     * @return
     */
    @Override
    public Page<ExpectedPositionInfoByPositionCodeVo> queryListByPositionCode(ExpectedPositionQueryDto expectedPositionQueryDto) {

        int pageNumber = expectedPositionQueryDto.getPageNumber();
        int pageSize = expectedPositionQueryDto.getPageSize();
        Long count = expectedPositionMapper.queryCountByPositionCode(expectedPositionQueryDto.getPositionCode());
        List<ExpectedPositionInfoByPositionCodeVo> list = expectedPositionMapper.queryListByPositionCode(expectedPositionQueryDto.getPositionCode(), pageNumber, pageSize);
        Page<ExpectedPositionInfoByPositionCodeVo> page = new Page<>();
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

}
