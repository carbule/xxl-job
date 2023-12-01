package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.po.EnterpriseWelfareLabel;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;

import java.util.List;

/**
 * <p>
 * 企业福利标签表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseWelfareLabelMapper extends BaseMapper<EnterpriseWelfareLabel> {

    /**
     * 查询所有企业福利标签列表
     *
     * @return
     */
    List<EnterpriseWelfareLabelDataVo> queryAllData();
}
