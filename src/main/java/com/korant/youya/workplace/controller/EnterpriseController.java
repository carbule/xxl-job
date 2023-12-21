package com.korant.youya.workplace.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.korant.youya.workplace.annotations.ExplanationDict;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import com.korant.youya.workplace.service.EnterpriseService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * 查询企业员工列表
     *
     * @param queryEmployeeListDto
     * @return
     */
    @PostMapping("/queryEmployeeList")
    @PreAuthorize("hasRole('admin')")
    public R<?> queryEmployeeList(@RequestBody @Valid QueryEmployeeListDto queryEmployeeListDto) {
        Page<EmployeeVo> page = enterpriseService.queryEmployeeList(queryEmployeeListDto);
        return R.success(page);
    }

    /**
     * 查询企业HR列表
     *
     * @param queryHRListDto
     * @return
     */
    @PostMapping("/queryHRList")
    @PreAuthorize("hasRole('admin')")
    public R<?> queryHRList(@RequestBody @Valid QueryHRListDto queryHRListDto) {
        Page<HRVo> page = enterpriseService.queryHRList(queryHRListDto);
        return R.success(page);
    }

    /**
     * 查询待审批列表
     *
     * @param queryPendingApprovalListDto
     * @return
     */
    @PostMapping("/queryPendingApprovalList")
    @PreAuthorize("hasRole('admin')")
    public R<?> queryPendingApprovalList(@RequestBody @Valid QueryPendingApprovalListDto queryPendingApprovalListDto) {
        Page<EnterprisePendingApprovalVo> page = enterpriseService.queryPendingApprovalList(queryPendingApprovalListDto);
        return R.success(page);
    }

    /**
     * 同意事项
     *
     * @return
     */
    @GetMapping("/agree/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> agree(@PathVariable("id") Long id) {
        enterpriseService.agree(id);
        return R.ok();
    }

    /**
     * 拒绝事项
     *
     * @return
     */
    @GetMapping("/refuse/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> refuse(@PathVariable("id") Long id) {
        enterpriseService.refuse(id);
        return R.ok();
    }

    /**
     * 撤销申请
     *
     * @return
     */
    @GetMapping("/revoke/{id}")
    public R<?> revoke(@PathVariable("id") Long id) {
        enterpriseService.revoke(id);
        return R.ok();
    }

    /**
     * 变更为hr
     *
     * @return
     */
    @GetMapping("/changeHR/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> changeHR(@PathVariable("id") Long id) {
        enterpriseService.changeHR(id);
        return R.ok();
    }

    /**
     * 变更为员工
     *
     * @return
     */
    @GetMapping("/changeEmployee/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> changeEmployee(@PathVariable("id") Long id) {
        enterpriseService.changeEmployee(id);
        return R.ok();
    }

    /**
     * 校验员工是否有职位
     *
     * @return
     */
    @GetMapping("/checkEmployeeHavePositions/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> checkEmployeeHavePositions(@PathVariable("id") Long id) {
        boolean result = enterpriseService.checkEmployeeHavePositions(id);
        return R.success(result);
    }

    /**
     * 强制移除员工
     *
     * @return
     */
    @GetMapping("/forceRemoveEmployee/{id}")
    @PreAuthorize("hasRole('admin')")
    public R<?> forceRemoveEmployee(@PathVariable("id") Long id) {
        enterpriseService.forceRemoveEmployee(id);
        return R.ok();
    }

    /**
     * 转让管理员
     *
     * @return
     */
    @PostMapping("/transferAdministrator")
    @PreAuthorize("hasRole('admin')")
    public R<?> transferAdministrator(@RequestBody @Valid TransferAdministratorDto transferAdministratorDto) {
        enterpriseService.transferAdministrator(transferAdministratorDto);
        return R.ok();
    }

    /**
     * 退出企业
     *
     * @return
     */
    @GetMapping("/exit")
    @PreAuthorize("hasAnyRole('hr','admin')")
    public R<?> exit() {
        enterpriseService.exit();
        return R.ok();
    }

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
     * 查询企业创建失败详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryCreateFailureDetail/{id}")
    public R<?> queryCreateFailureDetail(@PathVariable("id") Long id) {
        EnterpriseCreateFailureDetailVo createFailureDetailVo = enterpriseService.queryCreateFailureDetail(id);
        return R.success(createFailureDetailVo);
    }

    /**
     * 重新提交企业信息
     *
     * @param resubmitDto
     * @return
     */
    @PostMapping("/resubmit")
    public R<?> resubmit(@RequestBody @Valid EnterpriseResubmitDto resubmitDto) {
        enterpriseService.resubmit(resubmitDto);
        return R.success(resubmitDto);
    }

    /**
     * 根据企业名称查询企业
     *
     * @param nameDto
     * @return
     */
    @PostMapping("/queryEnterpriseByName")
    public R<?> queryEnterpriseByName(@RequestBody @Valid EnterpriseByNameDto nameDto) {
        List<EnterpriseByNameVo> nameVoList = enterpriseService.queryEnterpriseByName(nameDto);
        return R.success(nameVoList);
    }

    /**
     * 加入企业
     *
     * @param joinDto
     * @return
     */
    @PostMapping("/join")
    public R<?> join(@RequestBody @Valid EnterpriseJoinDto joinDto) {
        enterpriseService.join(joinDto);
        return R.ok();
    }

    /**
     * 修改企业logo
     *
     * @param
     * @return
     */
    @PostMapping("/modifyLogo")
    @PreAuthorize("hasRole('admin')")
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
    @PreAuthorize("hasRole('admin')")
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
    @GetMapping("/queryEnterpriseInfoByLoginUser")
    @ExplanationDict
    public R<?> queryEnterpriseInfoByLoginUser() {
        EnterpriseInfoByLoginUserVo infoByUserVo = enterpriseService.queryEnterpriseInfoByLoginUser();
        return R.success(infoByUserVo);
    }

    /**
     * 查询企业结构信息
     *
     * @return
     */
    @GetMapping("/queryEnterpriseStructureInfo")
    @PreAuthorize("hasAnyRole('hr','admin')")
    @ExplanationDict
    public R<?> queryEnterpriseStructureInfo() {
        EnterpriseStructureInfoVo structureInfoVo = enterpriseService.queryEnterpriseStructureInfo();
        return R.success(structureInfoVo);
    }

    /**
     * 查询企业基础信息
     *
     * @return
     */
    @GetMapping("/queryEnterpriseBasicInfo")
    @PreAuthorize("hasAnyRole('hr','admin')")
    @ExplanationDict
    public R<?> queryEnterpriseBasicInfo() {
        EnterpriseBasicInfoVo basicInfoVo = enterpriseService.queryEnterpriseBasicInfo();
        return R.success(basicInfoVo);
    }

    /**
     * 查询企业营业执照信息
     *
     * @param
     * @returnb'gen
     */
    @GetMapping("/queryEnterpriseBusinessLicense")
    @PreAuthorize("hasAnyRole('hr','admin')")
    public R<?> queryEnterpriseBusinessLicense() {
        EnterpriseBusinessLicenseVo businessLicenseVo = enterpriseService.queryEnterpriseBusinessLicense();
        return R.success(businessLicenseVo);
    }

    /**
     * 变更企业信息
     *
     * @param
     * @returnb'gen
     */
    @PostMapping("/changeEnterprise")
    @PreAuthorize("hasRole('admin')")
    public R<?> changeEnterprise(@RequestBody @Valid EnterpriseChangeDto changeDto) {
        enterpriseService.changeEnterprise(changeDto);
        return R.ok();
    }

    /**
     * 获取企业分享信息
     *
     * @param
     * @returnb'gen
     */
    @GetMapping("/getEnterpriseSharedInfo")
    @PreAuthorize("hasRole('admin')")
    public R<?> getSharedInfo() {
        EnterpriseSharedInfoVo enterpriseSharedInfoVo = enterpriseService.getSharedInfo();
        return R.success(enterpriseSharedInfoVo);
    }

    /**
     * 上传分享图片
     *
     * @param
     * @returnb'gen
     */
    @PostMapping("/uploadShareImage")
    @PreAuthorize("hasRole('admin')")
    public R<?> uploadShareImage(MultipartFile file) {
        String imageUrl = enterpriseService.uploadShareImage(file);
        return R.success(imageUrl);
    }
}
