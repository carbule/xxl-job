package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.DistrictDataMapper;
import com.korant.youya.workplace.pojo.po.DistrictData;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataTreeVo;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataVo;
import com.korant.youya.workplace.pojo.vo.district.QueryAllDataSortedByAcronymVo;
import com.korant.youya.workplace.service.DistrictDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class DistrictDataServiceImpl extends ServiceImpl<DistrictDataMapper, DistrictData> implements DistrictDataService {

    @Resource
    private DistrictDataMapper districtDataMapper;

    /**
     * 查询所有地区数据
     *
     * @return
     */
    @Override
    public List<DistrictDataTreeVo> queryAllData() {
        //查出所有地区信息
        List<DistrictDataTreeVo> districtDataTreeVos = districtDataMapper.queryAllData();
        //查出所有一级职位
        List<DistrictDataTreeVo> topDistricts = districtDataTreeVos.stream().filter(s -> s.getPid().equals(0L)).collect(Collectors.toList());
        topDistricts.forEach(s ->
                //递归存放子节点
                recursivePositionsFromList(s, districtDataTreeVos)
        );
        return topDistricts;
    }

    /**
     * 查询所有地区数据按缩略词排序
     *
     * @return
     */
    @Override
    public QueryAllDataSortedByAcronymVo queryAllDataSortedByAcronym() {
        List<DistrictDataVo> districtDataVoList = districtDataMapper.queryAllDataSortedByAcronym();
        Map<String, List<DistrictDataVo>> map = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toMap(key -> key, key -> new ArrayList<>()));
        districtDataVoList.forEach(s -> {
            List<DistrictDataVo> list = map.get(s.getAcronym());
            list.add(s);
        });
        QueryAllDataSortedByAcronymVo acronymVo = new QueryAllDataSortedByAcronymVo();
        acronymVo.setDistrictDataMap(map);
        return acronymVo;
    }

    /**
     * 从集合中筛选出子节点
     *
     * @param dataTreeVo
     * @param districtDataTreeVoList
     */
    private void recursivePositionsFromList(DistrictDataTreeVo dataTreeVo, List<DistrictDataTreeVo> districtDataTreeVoList) {
        List<DistrictDataTreeVo> childrenList = new ArrayList<>();
        //遍历所有数据，找到是入参父节点的子节点的数据，然后加到childrenList集合中。
        districtDataTreeVoList.forEach(s -> {
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
        for (DistrictDataTreeVo child :
                childrenList) {
            recursivePositionsFromList(child, districtDataTreeVoList);
        }
    }

    public static void main(String[] args) {
        Map<String, List<DistrictDataVo>> map = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toMap(key -> key, key -> new ArrayList<>()));

        // 打印生成的 Map
        System.out.println(map);
    }
}
