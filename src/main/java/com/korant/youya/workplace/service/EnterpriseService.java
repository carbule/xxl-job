package com.korant.youya.workplace.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
     * 查询企业hr列表
     *
     * @param queryHRListDto
     * @return
     */
    Page<HRVo> queryHRList(QueryHRListDto queryHRListDto);

    /**
     * 查询企业员工列表
     *
     * @param queryEmployeeListDto
     * @return
     */
    Page<EmployeeVo> queryEmployeeList(QueryEmployeeListDto queryEmployeeListDto);

    /**
     * 查询转让人员列表
     *
     * @param queryHRListDto
     * @return
     */
    Page<TransferPersonnelVo> queryTransferPersonnelList(QueryTransferPersonnelListDto queryHRListDto);

    /**
     * 转让职位
     *
     * @param transferJobDto
     */
    void transferJob(TransferJobDto transferJobDto);

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
     * 获取企业分享信息
     *
     * @return
     */
    EnterpriseSharedInfoVo getSharedInfo();

    /**
     * 上传分享图片
     *
     * @param file
     */
    String uploadShareImage(MultipartFile file);

    /**
     * 根据二维码id查询企业信息
     *
     * @param qrcodeIdDto
     * @return
     */
    EnterpriseInfoByQrcodeIdVo queryEnterpriseInfoByQrcodeId(QueryEnterpriseInfoByQrcodeIdDto qrcodeIdDto);

    /**
     * 删除邀请二维码
     *
     * @param id
     */
    void deleteInvitationQrcode(Long id);

    /**
     * 删除分享图片
     *
     * @param objectKey
     */
    void deleteShareImage(String objectKey);

    /**
     * 查询企业钱包信息
     *
     * @return
     */
    EnterpriseWalletVo queryEnterpriseWalletInfo();

    /**
     * 企业微信充值
     *
     * @param enterpriseRechargeDto
     * @return
     */
    JSONObject recharge(EnterpriseRechargeDto enterpriseRechargeDto);

    /**
     * 企业完成支付
     *
     * @param completePaymentDto
     */
    void completePayment(EnterpriseCompletePaymentDto completePaymentDto);

    /**
     * 企业充值通知
     *
     * @param request
     * @param response
     */
    void rechargeNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询企业充值结果
     *
     * @param rechargeResultDto
     * @return
     */
    Integer queryRechargeResult(QueryEnterpriseRechargeResultDto rechargeResultDto);
}
