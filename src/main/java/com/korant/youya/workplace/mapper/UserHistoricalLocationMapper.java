package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.UserHistoricalLocation;
import com.korant.youya.workplace.pojo.vo.userhistoricallocation.UserHistoricalLocationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户历史定位城市 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-26
 */
public interface UserHistoricalLocationMapper extends BaseMapper<UserHistoricalLocation> {

    /**
     * 查询用户最新定位
     *
     * @param userId
     * @return
     */
    UserHistoricalLocationVo queryLatestLocation(@Param("userId") Long userId);

    /**
     * 查询用户历史定位信息
     *
     * @param userId
     * @return
     */
    List<UserHistoricalLocationVo> queryHistoricalpositioning(@Param("userId") Long userId);
}
