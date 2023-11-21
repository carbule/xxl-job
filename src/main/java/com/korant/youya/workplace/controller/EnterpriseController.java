package com.korant.youya.workplace.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseDetailVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByNameVo;
import com.korant.youya.workplace.pojo.vo.enterprise.EnterpriseInfoByUserVo;
import com.korant.youya.workplace.service.EnterpriseService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 企业信息表 前端控制器
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Resource
    private EnterpriseService enterpriseService;

    /**
     * 获取营业执照信息
     *
     * @param file
     * @return
     */
    @PostMapping("/getBusinessLicenseInfo")
    public R<?> getBusinessLicenseInfo(MultipartFile file) {
        JSONObject object = enterpriseService.getBusinessLicenseInfo(file);
        return R.success(object);
    }

    /**
     * 创建企业
     *
     * @param createDto
     * @return
     */
    @PostMapping("/create")
    public R<?> create(@RequestBody @Valid EnterpriseCreateDto createDto) {
        enterpriseService.create(createDto);
        return R.ok();
    }

    /**
     * 修改企业logo
     *
     * @param
     * @return
     */
    @PostMapping("/modifyLogo")
    public R<?> modifyLogo(@RequestBody @Valid EnterpriseModifyLogoDto modifyLogoDto) {
        enterpriseService.modifyLogo(modifyLogoDto);
        return R.ok();
    }

    /**
     * 修改企业
     *
     * @param
     * @return
     */
    @PostMapping("/modify")
    public R<?> modify(@RequestBody @Valid EnterpriseModifyDto modifyDto) {
        enterpriseService.modify(modifyDto);
        return R.ok();
    }

    /**
     * 根据当前登陆用户查询企业信息
     *
     * @param
     * @return
     */
    @GetMapping("/queryEnterpriseInfoByUser")
    public R<?> queryEnterpriseInfoByUser() {
        EnterpriseInfoByUserVo infoByUserVo = enterpriseService.queryEnterpriseInfoByUser();
        return R.success(infoByUserVo);
    }

    /**
     * 查询企业详细信息
     *
     * @return
     */
    @GetMapping("/detail/{id}")
    public R<?> detail(@PathVariable("id") Long id) {
        EnterpriseDetailVo detailVo = enterpriseService.detail(id);
        return R.success(detailVo);
    }

    /**
     * 变更企业
     *
     * @param
     * @return
     */
    @PostMapping("/changeEnterpriseInfo")
    public R<?> changeEnterpriseInfo(@RequestBody @Valid EnterpriseChangeDto changeDto) {
        enterpriseService.changeEnterpriseInfo(changeDto);
        return R.ok();
    }

    /**
     * 关联企业-根据企业名称查询企业
     *
     * @return
     */
    @PostMapping("/getEnterpriseByName")
    public R<?> getEnterpriseByName(@RequestBody @Valid EnterpriseQueryListDto enterpriseQueryListDto) {
        Page<EnterpriseInfoByNameVo> page = enterpriseService.getEnterpriseByName(enterpriseQueryListDto);
        return R.success(page);
    }

    /**
     * 解除关联企业绑定
     *
     * @return
     */
    @GetMapping("/Unbinding/{id}")
    public R<?> Unbinding(@PathVariable("id") Long id) {
        enterpriseService.Unbinding(id);
        return R.ok();
    }

}
