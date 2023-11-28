package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoCreateDto;
import com.korant.youya.workplace.pojo.dto.enterprisetodo.EnterpriseTodoListDto;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseEmployeeListVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprisetodo.EnterpriseTodoListVo;
import com.korant.youya.workplace.service.EnterpriseTodoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 企业代办事项表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/enterpriseTodo")
public class EnterpriseTodoController {

    @Resource
    private EnterpriseTodoService enterpriseTodoService;

    /**
     * 创建加入公司申请
     *
     * @param
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid EnterpriseTodoCreateDto enterpriseTodoCreateDto) {
        enterpriseTodoService.create(enterpriseTodoCreateDto);
        return R.ok();
    }

    /**
     * 获取当前用户的加入公司申请
     *
     * @return
     */
    @GetMapping("/getEnterpriseTodoByUser")
    @ExplanationDict
    public R<?> getEnterpriseTodoByUser() {
        EnterpriseTodoDetailVo enterpriseTodoDetailVo = enterpriseTodoService.getEnterpriseTodoByUser();
        return R.success(enterpriseTodoDetailVo);
    }

    /**
     * 管理员查看加入公司申请列表
     *
     * @param
     * @return
     */
    @PostMapping("/queryApprovalList")
    public R<?> queryApprovalList(@RequestBody EnterpriseTodoListDto enterpriseTodoListDto) {
        Page<EnterpriseTodoListVo> page = enterpriseTodoService.queryApprovalList(enterpriseTodoListDto);
        return R.success(page);
    }

    /**
     * 管理员查看成员列表(HR)
     *
     * @param
     * @return
     */
    @PostMapping("/queryEmployeeList")
    public R<?> queryEmployeeList(@RequestBody EnterpriseTodoListDto enterpriseTodoListDto) {
        Page<EnterpriseEmployeeListVo> page = enterpriseTodoService.queryEmployeeList(enterpriseTodoListDto);
        return R.success(page);
    }


    /**
     * 同意用户的加入公司申请
     *
     * @return
     */
    @GetMapping("/pass/{id}")
    public R<?> pass(@PathVariable("id") Long id) {
        enterpriseTodoService.pass(id);
        return R.ok();
    }

    /**
     * 拒绝用户的加入公司申请
     *
     * @return
     */
    @GetMapping("/refuse/{id}")
    public R<?> refuse(@PathVariable("id") Long id) {
        enterpriseTodoService.refuse(id);
        return R.ok();
    }

}
