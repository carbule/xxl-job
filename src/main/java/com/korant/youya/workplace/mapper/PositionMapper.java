package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 职位信息表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface PositionMapper extends BaseMapper<Position> {

    /**
     * 查询所有职位数据
     *
     * @return
     */
    List<PositionDataTreeVo> queryAllData();

    /**
     * 查询所有行业
     *
     * @return
     */
    List<PositionDataVo> queryAllIndustries();

    /**
     * 根据行业code查询所有职位
     *
     * @param industryId
     * @return
     */
    List<PositionDataTreeVo> queryPositionsByIndustryId(@Param("industryId") Long industryId);
}
