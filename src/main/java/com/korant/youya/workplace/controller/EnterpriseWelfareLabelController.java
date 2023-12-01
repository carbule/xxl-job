package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.vo.enterprisewelfarelabel.EnterpriseWelfareLabelDataVo;
import com.korant.youya.workplace.service.EnterpriseWelfareLabelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 企业福利标签表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/enterpriseWelfareLabel")
public class EnterpriseWelfareLabelController {

    @Resource
    private EnterpriseWelfareLabelService enterpriseWelfareLabelService;

    /**
     * 查询所有企业福利标签列表
     *
     * @return
     */
    @GetMapping("/queryAllData")
    public R<?> queryAllData() {
        List<EnterpriseWelfareLabelDataVo> list = enterpriseWelfareLabelService.queryAllData();
        return R.success(list);
    }
}
