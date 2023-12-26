package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.userhistoricallocation.UserHistoricalLocationCreateDto;
import com.korant.youya.workplace.pojo.po.UserHistoricalLocation;
import com.korant.youya.workplace.pojo.vo.userhistoricallocation.UserHistoricalLocationVo;

/**
 * <p>
 * 用户历史定位城市 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-12-26
 */
public interface UserHistoricalLocationService extends IService<UserHistoricalLocation> {

    /**
     * 创建用户历史定位信息
     *
     * @param createDto
     */
    void create(UserHistoricalLocationCreateDto createDto);

    /**
     * 查询用户最新定位
     *
     * @return
     */
    UserHistoricalLocationVo queryLatestLocation();
}
