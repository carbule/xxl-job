package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.service.ExpectedPositionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户意向职位表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-17
 */
@RestController
@RequestMapping("/expectedPosition")
public class ExpectedPositionController {

    @Resource
    private ExpectedPositionService expectedPositionService;

    /**
     * 删除意向职位前校验
     *
     * @param id
     * @return
     */
    @GetMapping("/checkBeforeDelete/{id}")
    public R<?> checkBeforeDelete(@PathVariable("id") Long id) {
        boolean result = expectedPositionService.checkBeforeDelete(id);
        return R.success(result);
    }
}
