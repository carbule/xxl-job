package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.service.UserEnterpriseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户企业关联表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/userEnterprise")
public class UserEnterpriseController {

    @Resource
    private UserEnterpriseService userEnterpriseService;

    /**
     * 解除关联企业绑定
     *
     * @return
     */
    @GetMapping("/unbinding/{id}")
    public R<?> unbinding(@PathVariable("id") Long id) {
        userEnterpriseService.unbinding(id);
        return R.ok();
    }

    /**
     * 退出公司
     *
     * @return
     */
    @GetMapping("/exit/{id}")
    public R<?> exit(@PathVariable("id") Long id) {
        userEnterpriseService.exit(id);
        return R.ok();
    }

}
