package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionClassLevelVO;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;

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
     * 查询所有行业
     *
     * @return
     */
    List<PositionDataVo> queryAllIndustries();

    /**
     * 根据行业code查询所有职位
     *
     * @param industryCode
     * @return
     */
    List<PositionDataTreeVo> queryPositionsByIndustryCode(String industryCode);

    /**
     * 根据父级编码查询子集
     *
     * @param parentCode 父级编码
     * @param level      职位层级
     * @return {@link PositionDataVo}
     */
    List<PositionDataVo> listPositionsByParent(String parentCode, Integer level);

    /**
     * 查询专业领域下的职业等级
     *
     * @param sectorCode 领域 ID
     * @return {@link PositionClassLevelVO}
     */
    List<PositionClassLevelVO> listClassLevels(String sectorCode);
}
