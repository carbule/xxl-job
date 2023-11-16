package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.IntendedPosition;
import com.korant.youya.workplace.pojo.vo.intendedposition.IntendedPositionInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户意向职位表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface IntendedPositionMapper extends BaseMapper<IntendedPosition> {

    List<IntendedPositionInfoVo> findIntendedPositionInfo(@Param("userId") Long userId);
}
