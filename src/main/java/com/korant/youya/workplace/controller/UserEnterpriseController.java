package com.korant.youya.workplace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseQueryListDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseRemoveDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseTransferDto;
import com.korant.youya.workplace.pojo.vo.userenterprise.UserEnterpriseColleagueInfoVo;
import com.korant.youya.workplace.service.UserEnterpriseService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/unbinding")
    public R<?> unbinding() {
        userEnterpriseService.unbinding();
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

    /**
     * 管理员移除公司下用户(HR)
     *
     * @param
     * @return
     */
    @PostMapping("/removeUser")
    public R<?> removeUser(@RequestBody @Valid UserEnterpriseRemoveDto userEnterpriseRemoveDto) {
        userEnterpriseService.removeUser(userEnterpriseRemoveDto);
        return R.ok();
    }

    /**
     * 根据姓名查询公司同事
     *
     * @param
     * @return
     */
    @PostMapping("/queryColleagueByName")
    public R<?> queryColleagueByName(@RequestBody @Valid UserEnterpriseQueryListDto userEnterpriseQueryListDto) {
        Page<UserEnterpriseColleagueInfoVo> page = userEnterpriseService.queryColleagueByName(userEnterpriseQueryListDto);
        return R.success(page);
    }

    /**
     * 转让公司
     *
     * @param
     * @return
     */
    @PostMapping("/transfer")
    public R<?> transfer(@RequestBody @Valid UserEnterpriseTransferDto userEnterpriseQueryListDto) {
        userEnterpriseService.transfer(userEnterpriseQueryListDto);
        return R.ok();
    }

    /**
     * 判断当前人员是否有关联公司
     *
     * @param
     * @return
     */
    @GetMapping("/isLimit")
    public R<?> isLimit() {
        Integer status = userEnterpriseService.isLimit();
        return R.success(status);
    }

}
