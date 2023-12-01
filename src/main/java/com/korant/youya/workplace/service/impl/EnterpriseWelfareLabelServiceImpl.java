package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.mapper.EnterpriseWelfareLabelMapper;
import com.korant.youya.workplace.pojo.po.EnterpriseWelfareLabel;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;
import com.korant.youya.workplace.service.EnterpriseWelfareLabelService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业福利标签表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class EnterpriseWelfareLabelServiceImpl extends ServiceImpl<EnterpriseWelfareLabelMapper, EnterpriseWelfareLabel> implements EnterpriseWelfareLabelService {

    @Resource
    private EnterpriseWelfareLabelMapper enterpriseWelfareLabelMapper;

    /**
     * 查询所有企业福利标签列表
     *
     * @return
     */
    @Override
    public List<EnterpriseWelfareLabelDataVo> queryAllData() {
        return enterpriseWelfareLabelMapper.queryAllData();
    }
}
