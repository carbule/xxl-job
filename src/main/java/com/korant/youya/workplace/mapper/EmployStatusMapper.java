package com.korant.youya.workplace.mapper;

import com.korant.youya.workplace.pojo.po.EmployStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.vo.employstatus.EmployStatusVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 求职状态表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
public interface EmployStatusMapper extends BaseMapper<EmployStatus> {

    /**
     * 查询求职状态
     *
     * @param
     **/
    EmployStatusVo status(@Param("userId") Long userId);
}
