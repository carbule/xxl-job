package com.korant.youya.workplace.controller;

import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryEnterpriseByNameDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryPersonalBlockedEnterpriseDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.UserBlockedEnterpriseCreateDto;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.PersonalBlockedEnterpriseVo;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.QueryEnterpriseByNameVo;
import com.korant.youya.workplace.service.UserBlockedEnterpriseService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户屏蔽企业信息表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-23
 */
@RestController
@RequestMapping("/userBlockedEnterprise")
public class UserBlockedEnterpriseController {

    @Resource
    private UserBlockedEnterpriseService userBlockedEnterpriseService;

    /**
     * 查询个人屏蔽的企业
     *
     * @return
     */
    @PostMapping("/queryPersonalBlockedEnterprise")
    public R<?> queryPersonalBlockedEnterprise(@RequestBody @Valid QueryPersonalBlockedEnterpriseDto personalBlockedEnterpriseDto) {
         PageData<PersonalBlockedEnterpriseVo> pageData  = userBlockedEnterpriseService.queryPersonalBlockedEnterprise(personalBlockedEnterpriseDto);
        return R.success(pageData);
    }

    /**
     * 根据企业名称查询企业
     *
     * @return
     */
    @PostMapping("/queryEnterpriseByName")
    public R<?> queryEnterpriseByName(@RequestBody @Valid QueryEnterpriseByNameDto queryEnterpriseByNameDto) {
        QueryEnterpriseByNameVo enterpriseByNameVo = userBlockedEnterpriseService.queryEnterpriseByName(queryEnterpriseByNameDto);
        return R.success(enterpriseByNameVo);
    }

    /**
     * 创建屏蔽企业
     *
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid UserBlockedEnterpriseCreateDto userBlockedEnterpriseCreateDto) {
        userBlockedEnterpriseService.create(userBlockedEnterpriseCreateDto);
        return R.ok();
    }

    /**
     * 删除屏蔽企业
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        userBlockedEnterpriseService.delete(id);
        return R.ok();
    }
}
