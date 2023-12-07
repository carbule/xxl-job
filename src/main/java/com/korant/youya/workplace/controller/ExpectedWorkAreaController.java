package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.service.ExpectedWorkAreaService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户期望工作区域表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/expectedWorkArea")
public class ExpectedWorkAreaController {

    @Resource
    private ExpectedWorkAreaService expectedWorkAreaService;

    /**
     * 删除意向区域前校验
     *
     * @param id
     * @return
     */
    @GetMapping("/checkBeforeDelete/{id}")
    public R<?> checkBeforeDelete(@PathVariable("id") Long id) {
        boolean result = expectedWorkAreaService.checkBeforeDelete(id);
        return R.success(result);
    }
}
