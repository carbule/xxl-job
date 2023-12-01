package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.EnterpriseWelfareLabel;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;

import java.util.List;

/**
 * <p>
 * 企业福利标签表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseWelfareLabelService extends IService<EnterpriseWelfareLabel> {

    /**
     * 查询所有企业福利标签列表
     *
     * @return
     */
    List<EnterpriseWelfareLabelDataVo> queryAllData();
}
