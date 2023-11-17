package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionModifyDto;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.mapper.ExpectedPositionMapper;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import com.korant.youya.workplace.service.ExpectedPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
     * @Author Duan-zhixiao
     * @Description 查询用户的所有意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public List<ExpectedPositionInfoVo> findExpectedPositionInfo() {

        Long userId = 1L;

        return expectedPositionMapper.findExpectedPositionInfo(userId);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 添加意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void create(ExpectedPositionCreateDto expectedPositionCreateDto) {

        Long count = expectedPositionMapper.selectCount(new LambdaQueryWrapper<ExpectedPosition>().eq(ExpectedPosition::getStatusId, expectedPositionCreateDto.getStatusId()));
        if (count >= 5L)  throw new YouyaException("意向职位最多5个！");

        ExpectedPosition expectedPosition = new ExpectedPosition();
        BeanUtils.copyProperties(expectedPositionCreateDto, expectedPosition);
        expectedPositionMapper.insert(expectedPosition);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 编辑意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void modify(ExpectedPositionModifyDto expectedPositionModifyDto) {

        ExpectedPosition expectedPosition = expectedPositionMapper.selectById(expectedPositionModifyDto.getId());
        if (expectedPosition == null)   throw new YouyaException("意向职位不存在！");
        BeanUtils.copyProperties(expectedPositionModifyDto, expectedPosition);
        expectedPositionMapper.updateById(expectedPosition);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 删除意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void delete(Long id) {

        ExpectedPosition expectedPosition = expectedPositionMapper.selectById(id);
        if (expectedPosition == null)   throw new YouyaException("意向职位不存在！");
        expectedPosition.setIsDelete(1);
        expectedPositionMapper.updateById(expectedPosition);

    }
}
