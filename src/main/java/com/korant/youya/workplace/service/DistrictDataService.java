package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.DistrictData;
import com.korant.youya.workplace.pojo.vo.district.DistrictDataTreeVo;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface DistrictDataService extends IService<DistrictData> {

    List<DistrictDataTreeVo> queryAllData();
}
