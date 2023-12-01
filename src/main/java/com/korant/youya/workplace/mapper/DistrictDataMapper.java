package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.DistrictData;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataTreeVo;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 地区表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface DistrictDataMapper extends BaseMapper<DistrictData> {

    /**
     * 查询所有地区数据
     *
     * @return
     */
    List<DistrictDataTreeVo> queryAllData();

    /**
     * 查询所有地区数据按缩略词排序
     *
     * @return
     */
    List<DistrictDataVo> queryAllDataSortedByAcronym();

    /**
     * 根据省市县code查询地址
     *
     * @param provinceCode
     * @param cityCode
     * @param districtCode
     * @return
     */
    String searchAddressByCode(@Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode, @Param("districtCode") String districtCode);
}
