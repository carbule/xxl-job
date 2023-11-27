package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionData;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
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

    List<PositionData> queryIndustry();

    List<PositionData> querySector(@Param("code") String code);

}
