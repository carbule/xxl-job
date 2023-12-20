package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseService extends IService<Enterprise> {

    /**
     * 查询企业员工列表
     *
     * @param queryEmployeeListDto
     * @return
     */
    Page<EmployeeVo> queryEmployeeList(QueryEmployeeListDto queryEmployeeListDto);

    /**
     * 查询企业HR列表
     *
     * @param queryHRListDto
     * @return
     */
    Page<HRVo> queryHRList(QueryHRListDto queryHRListDto);

    /**
     * 查询待审批列表
     *
     * @param queryPendingApprovalListDto
     * @return
     */
    Page<EnterprisePendingApprovalVo> queryPendingApprovalList(QueryPendingApprovalListDto queryPendingApprovalListDto);

    /**
     * 同意事项
     *
     * @param id
     */
    void agree(Long id);

    /**
     * 拒绝事项
     *
     * @param id
     */
    void refuse(Long id);

    /**
     * 撤销申请
     *
     * @param id
     */
    void revoke(Long id);

    /**
     * 变更为hr
     *
     * @param id
     */
    void changeHR(Long id);

    /**
     * 变更为员工
     *
     * @param id
     */
    void changeEmployee(Long id);

    /**
     * 校验员工是否有职位
     *
     * @param id
     * @return
     */
    boolean checkEmployeeHavePositions(Long id);

    /**
     * 强制移除员工
     *
     * @param id
     */
    void forceRemoveEmployee(Long id);

    /**
     * 转让管理员
     */
    void transferAdministrator(TransferAdministratorDto transferAdministratorDto);

    /**
     * 退出企业
     */
    void exit();

    /**
     * 获取营业执照信息
     *
     * @param file
     * @return
     */
    JSONObject getBusinessLicenseInfo(MultipartFile file);

    /**
     * 创建企业
     *
     * @param createDto
     */
    void create(EnterpriseCreateDto createDto);

    /**
     * 查询企业创建失败详情
     *
     * @param id
     * @return
     */
    EnterpriseCreateFailureDetailVo queryCreateFailureDetail(Long id);

    /**
     * 重新提交企业信息
     *
     * @param resubmitDto
     */
    void resubmit(EnterpriseResubmitDto resubmitDto);

    /**
     * 根据企业名称查询企业
     *
     * @param nameDto
     * @return
     */
    List<EnterpriseByNameVo> queryEnterpriseByName(EnterpriseByNameDto nameDto);

    /**
     * 加入企业
     *
     * @param joinDto
     */
    void join(EnterpriseJoinDto joinDto);

    /**
     * 修改企业logo
     *
     * @param modifyLogoDto
     */
    void modifyLogo(EnterpriseModifyLogoDto modifyLogoDto);

    /**
     * 修改企业
     *
     * @param modifyDto
     */
    void modify(EnterpriseModifyDto modifyDto);

    /**
     * 根据当前登陆用户查询企业信息
     *
     * @return
     */
    EnterpriseInfoByLoginUserVo queryEnterpriseInfoByLoginUser();

    /**
     * 查询企业结构信息
     *
     * @return
     */
    EnterpriseStructureInfoVo queryEnterpriseStructureInfo();

    /**
     * 查询企业基础信息
     *
     * @return
     */
    EnterpriseBasicInfoVo queryEnterpriseBasicInfo();

    /**
     * 查询企业营业执照信息
     *
     * @return
     */
    EnterpriseBusinessLicenseVo queryEnterpriseBusinessLicense();

    /**
     * 变更企业信息
     *
     * @param changeDto
     */
    void changeEnterprise(EnterpriseChangeDto changeDto);

    /**
     * 获取邀请二维码
     *
     * @return
     */
    String getInvitationQrcode();
}
