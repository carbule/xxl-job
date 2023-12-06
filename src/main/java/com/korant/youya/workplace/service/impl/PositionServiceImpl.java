package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.PositionMapper;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionDataTreeVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
