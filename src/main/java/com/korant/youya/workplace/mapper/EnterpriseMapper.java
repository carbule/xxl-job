package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.enterprise.*;
import com.korant.youya.workplace.pojo.po.Enterprise;
import com.korant.youya.workplace.pojo.vo.enterprise.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 企业信息表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    /**
     * 查询企业员工数量
     *
     * @param userId
     * @param enterpriseId
     * @return
     */
    int queryEmployeeListCount(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId);

    /**
     * 查询企业员工列表
     *
     * @param userId
     * @param enterpriseId
     * @param queryEmployeeListDto
     * @return
     */
    List<EmployeeVo> queryEmployeeList(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId, @Param("queryEmployeeListDto") QueryEmployeeListDto queryEmployeeListDto);

    /**
     * 查询企业代办事项
     *
     * @param enterpriseId
     * @param queryPendingApprovalListDto
     * @return
     */
    List<EnterprisePendingApprovalVo> queryPendingApprovalList(@Param("enterpriseId") Long enterpriseId, @Param("queryPendingApprovalListDto") QueryPendingApprovalListDto queryPendingApprovalListDto);

    /**
     * 根据企业名称查询企业
     *
     * @param nameDto
     * @return
     */
    List<EnterpriseByNameVo> queryEnterpriseByName(@Param("nameDto") EnterpriseByNameDto nameDto);

    /**
     * 修改企业logo
     *
     * @param modifyLogoDto
     * @return
     */
    int modifyLogo(@Param("modifyLogoDto") EnterpriseModifyLogoDto modifyLogoDto);

    /**
     * 修改企业
     *
     * @param modifyDto
     * @return
     */
    int modify(@Param("modifyDto") EnterpriseModifyDto modifyDto);

    /**
     * 根据hr身份查询企业信息
     *
     * @param userId
     * @param enterpriseId
     * @return
     */
    EnterpriseInfoByLoginUserVo queryEnterpriseInfoByHR(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId);

    /**
     * 根据管理员身份查询企业信息
     *
     * @param enterpriseId
     * @return
     */
    EnterpriseInfoByLoginUserVo queryEnterpriseInfoByAdmin(@Param("enterpriseId") Long enterpriseId);

    /**
     * 根据管理员身份查询企业结构信息
     *
     * @param enterpriseId
     * @return
     */
    EnterpriseStructureInfoVo queryEnterpriseStructureInfoByAdmin(@Param("enterpriseId") Long enterpriseId);

    /**
     * 根据hr身份查询企业结构信息
     *
     * @param enterpriseId
     * @return
     */
    EnterpriseStructureInfoVo queryEnterpriseStructureInfoByHR(@Param("enterpriseId") Long enterpriseId);

    /**
     * 查询企业基础信息
     *
     * @param enterpriseId
     * @return
     */
    EnterpriseBasicInfoVo queryEnterpriseBasicInfo(@Param("enterpriseId") Long enterpriseId);
}
