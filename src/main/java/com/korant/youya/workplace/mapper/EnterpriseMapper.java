package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.enterprise.EnterpriseChangeDto;
import com.korant.youya.workplace.pojo.dto.enterprise.EnterpriseModifyDto;
import com.korant.youya.workplace.pojo.dto.enterprise.EnterpriseModifyLogoDto;
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
     * @Description 修改企业logo
     * @Param
     * @return
     **/
    int modifyLogo(@Param("modifyLogoDto") EnterpriseModifyLogoDto modifyLogoDto);

    /**
     * @Description 修改企业
     * @Param
     * @return
     **/
    int modify(@Param("modifyDto") EnterpriseModifyDto modifyDto);

    /**
     * @Description 根据当前登陆用户查询企业信息
     * @Param
     * @return
     **/
    EnterpriseInfoByUserVo queryEnterpriseInfoByUser(@Param("userId") Long userId);

    /**
     * @Description 查询企业详细信息
     * @Param
     * @return
     **/
    EnterpriseDetailVo detail(@Param("id") Long id);

    /**
     * @Description 根据企业名称查询企业
     * @Param
     * @return
     **/
    List<EnterpriseInfoByNameVo> getEnterpriseByName(@Param("name") String name, @Param("status") Integer status);

    /**
     * @Description 变更企业信息
     * @Param
     * @return
     **/
    int changeEnterpriseInfo(@Param("changeDto") EnterpriseChangeDto changeDto);

    /**
     * @Description 查询企业hr跟员工总数
     * @Param
     * @return
     **/
    List<EnterpriseHrAndEmployeeInfoVo> getHrAndEmployeeTotal(@Param("id") Long id);

}
