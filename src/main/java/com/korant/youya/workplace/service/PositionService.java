package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionData;
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

    /**
     * 查询所有行位
     *
     * @return
     */
    List<PositionData> queryIndustry();

//    /**
//     * 根据行位查询所有领域
//     *
//     * @return
//     */
//    List<PositionData> querySector(String code);

    /**
     * 根据领域查询所有职位
     *
     * @return
     */
    List<PositionData> queryPosition(String code);
}
