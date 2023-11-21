package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.expectedposition.ExpectedPositionCreateDto;
import com.korant.youya.workplace.pojo.po.ExpectedPosition;
import com.korant.youya.workplace.pojo.vo.expectedposition.ExpectedPositionInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户意向职位表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface ExpectedPositionMapper extends BaseMapper<ExpectedPosition> {

    /**
     * @Description 查询用户的所有意向职位
     * @Param
     * @return
     **/
    List<ExpectedPositionInfoVo> findExpectedPositionInfo(@Param("userId") Long userId);

    void insertBatch(@Param("positionCreateDtoList") List<ExpectedPositionCreateDto> positionCreateDtoList, @Param("id") Long id);
}
