package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.IntendedPositionMapper;
import com.korant.youya.workplace.pojo.SessionLocal;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionCreateDto;
import com.korant.youya.workplace.pojo.dto.intendedposition.IntendedPositionModifyDto;
import com.korant.youya.workplace.pojo.po.IntendedPosition;
import com.korant.youya.workplace.pojo.vo.intendedposition.IntendedPositionInfoVo;
import com.korant.youya.workplace.service.IntendedPositionService;
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
 * @since 2023-11-14
 */
@Service
public class IntendedPositionServiceImpl extends ServiceImpl<IntendedPositionMapper, IntendedPosition> implements IntendedPositionService {

    @Resource
    private IntendedPositionMapper intendedPositionMapper;

    /**
     * @Author Duan-zhixiao
     * @Description 查询用户的所有意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public List<IntendedPositionInfoVo> findIntendedPositionInfo() {

        Long userId = SessionLocal.getUserId();

        return intendedPositionMapper.findIntendedPositionInfo(userId);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 添加意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void create(IntendedPositionCreateDto intendedPositionCreateDto) {

        Long count = intendedPositionMapper.selectCount(new LambdaQueryWrapper<IntendedPosition>().eq(IntendedPosition::getIntentionId, intendedPositionCreateDto.getIntentionId()));
        if (count >= 5L)  throw new YouyaException("意向职位最多5个！");

        IntendedPosition intendedPosition = new IntendedPosition();
        BeanUtils.copyProperties(intendedPositionCreateDto, intendedPosition);
        intendedPositionMapper.insert(intendedPosition);

    }

    /**
     * @Author Duan-zhixiao
     * @Description 编辑意向职位
     * @Date 14:31 2023/11/16
     * @Param
     * @return
     **/
    @Override
    public void modify(IntendedPositionModifyDto intendedPositionIncreaseDto) {

        IntendedPosition intendedPosition = intendedPositionMapper.selectById(intendedPositionIncreaseDto.getId());
        if (intendedPosition == null)   throw new YouyaException("意向职位不存在！");
        BeanUtils.copyProperties(intendedPositionIncreaseDto, intendedPosition);
        intendedPositionMapper.updateById(intendedPosition);

    }

    @Override
    public void delete(Long id) {

        IntendedPosition intendedPosition = intendedPositionMapper.selectById(id);
        if (intendedPosition == null)   throw new YouyaException("意向职位不存在！");
        intendedPosition.setIsDelete(1);
        intendedPositionMapper.updateById(intendedPosition);

    }

}
