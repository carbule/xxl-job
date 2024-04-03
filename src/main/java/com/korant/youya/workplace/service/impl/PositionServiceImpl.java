package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.PositionLevelEnum;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.PositionMapper;
import com.korant.youya.workplace.pojo.po.Position;
import com.korant.youya.workplace.pojo.vo.position.PositionClassLevelVO;
import com.korant.youya.workplace.pojo.vo.position.PositionDataVo;
import com.korant.youya.workplace.service.PositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
     * 查询所有行业
     *
     * @return
     */
    @Override
    public List<PositionDataVo> queryAllIndustries() {
        return positionMapper.queryAllIndustries();
    }

    @Override
    public List<PositionDataVo> listPositionsByParent(String parentCode, Integer level) {
        Position parent = positionMapper.selectOne(new LambdaQueryWrapper<Position>().eq(Position::getCode, parentCode));
        if (parent == null) {
            throw new YouyaException("父级职位不存在");
        }
        List<Position> positions = positionMapper.selectList(new LambdaQueryWrapper<Position>()
                .eq(Position::getPid, parent.getId())
                .eq(level != null, Position::getLevel, level)
                .orderByAsc(Position::getCode));

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
                            .setDescription(e.getDescription()))
//                    .sorted(Comparator.comparing(PositionClassLevelVO.ClassLevel::getClassLevel))
                    .sorted((o1, o2) -> {
                        String o1ClassLevel = o1.getClassLevel();
                        String o2ClassLevel = o2.getClassLevel();

                        // 专业岗位 - 初级（P1）
                        // 截取等级编码部分进行排序
                        String o1ClassLevelNo = o1ClassLevel.substring(o1ClassLevel.lastIndexOf("（"), o1ClassLevel.lastIndexOf("）"));
                        String o2ClassLevelNo = o2ClassLevel.substring(o2ClassLevel.lastIndexOf("（"), o2ClassLevel.lastIndexOf("）"));
                        return o1ClassLevelNo.compareTo(o2ClassLevelNo);
                    })

                    .toList());
            returns.add(vo);
        });
        returns.sort(Comparator.comparing(PositionClassLevelVO::getOrganizationLevel));
        return returns;
    }
}
