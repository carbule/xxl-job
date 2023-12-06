package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;

import java.util.List;

/**
 * <p>
 * 职位信息表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface PositionService extends IService<Position> {

    /**
     * 查询所有职位数据
     *
     * @return
     */
    List<PositionDataTreeVo> queryAllData();
}
