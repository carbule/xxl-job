package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.PositionLevelEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.PositionMapper;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionClassLevelVO;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


/**
 * <p>
 * 职位信息表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Resource
    private PositionMapper positionMapper;

    /**
     * 查询所有职位数据
     *
     * @return
     */
    @Override
    public List<PositionDataTreeVo> queryAllData() {
        //查询出所有职位信息
        List<PositionDataTreeVo> positionDataTreeVos = positionMapper.queryAllData();
        //查出所有一级职位
        List<PositionDataTreeVo> topPositions = positionDataTreeVos.stream().filter(s -> s.getPid().equals(0L)).collect(Collectors.toList());
        topPositions.forEach(s ->
                //递归存放子节点
                recursivePositionsFromList(s, positionDataTreeVos)
        );
        return topPositions;
    }

    /**
     * 查询所有行业
     *
     * @return
     */
    @Override
    public List<PositionDataVo> queryAllIndustries() {
        return positionMapper.queryAllIndustries();
    }

    /**
     * 根据行业code查询所有职位
     *
     * @param industryCode
     * @return
     */
    @Override
    public List<PositionDataTreeVo> queryPositionsByIndustryCode(String industryCode) {
        Position position = positionMapper.selectOne(new LambdaQueryWrapper<Position>().eq(Position::getCode, industryCode).eq(Position::getIsDelete, 0));
        if (null == position) throw new YouyaException("行业不存在");
        return positionMapper.queryPositionsByIndustryId(position.getId());
    }

    @Override
    public List<PositionDataVo> listPositionsByParent(String parentCode, Integer level) {
        Position parent = positionMapper.selectOne(new LambdaQueryWrapper<Position>().eq(Position::getCode, parentCode));
        if (parent == null) {
            throw new YouyaException("父级职位不存在");
        }
        List<Position> positions = positionMapper.selectList(new LambdaQueryWrapper<Position>()
                .eq(Position::getPid, parent.getId())
                .eq(level != null, Position::getLevel, level));

        return positions.stream().map(e -> new PositionDataVo()
                .setId(e.getId())
                .setPid(e.getPid())
                .setName(e.getName())
                .setCode(e.getCode())
                .setLevel(e.getLevel())).toList();
    }

    @Override
    public List<PositionClassLevelVO> listClassLevels(String sectorCode) {
        Position sector = positionMapper.selectOne(new LambdaQueryWrapper<Position>().eq(Position::getCode, sectorCode));
        if (sector == null) {
            throw new YouyaException("领域不存在");
        }

        // 获取该领域下所有职位
        List<Position> positions = positionMapper.selectList(new LambdaQueryWrapper<Position>()
                .eq(Position::getPid, sector.getId())
                .eq(Position::getLevel, PositionLevelEnum.LEVEL_4.getValue()));
        // 根据职业群分组
        Map<String, List<Position>> positionsGroup = positions.stream().collect(Collectors.groupingBy(Position::getOrganizationLevel));

        List<PositionClassLevelVO> returns = new ArrayList<>();
        positionsGroup.forEach((k, v) -> {
            PositionClassLevelVO vo = new PositionClassLevelVO();
            vo.setOrganizationLevel(k);
            vo.setClassLevels(v.stream().map(e -> new PositionClassLevelVO.ClassLevel()
                    .setClassLevel(e.getClassLevel())
                    .setPositionCode(e.getCode())
                    .setPositionName(e.getName())
                    .setDescription(e.getDescription())).toList());
            returns.add(vo);
        });
        return returns;
    }

    /**
     * 从集合中筛选出子节点
     *
     * @param dataTreeVo
     * @param positionDataTreeVoList
     */
    private void recursivePositionsFromList(PositionDataTreeVo dataTreeVo, List<PositionDataTreeVo> positionDataTreeVoList) {
        List<PositionDataTreeVo> childrenList = new ArrayList<>();
        //遍历所有数据，找到是入参父节点的子节点的数据，然后加到childrenList集合中。
        positionDataTreeVoList.forEach(s -> {
            if (dataTreeVo.getId().equals(s.getPid())) {
                childrenList.add(s);
            }
        });
        //若子节点不存在，那么就不必再遍历子节点中的子节点了 直接返回。
        if (childrenList.size() == 0)
            return;
        //设置父节点的子节点列表
        dataTreeVo.setChildren(childrenList);
        //若子节点存在，接着递归调用该方法，寻找子节点的子节点。
        for (PositionDataTreeVo child :
                childrenList) {
            recursivePositionsFromList(child, positionDataTreeVoList);
        }
    }
}
