package com.korant.youya.workplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryEnterpriseByNameDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryPersonalBlockedEnterpriseDto;
import com.korant.youya.workplace.pojo.po.UserBlockedEnterprise;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.PersonalBlockedEnterpriseVo;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.QueryEnterpriseByNameVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户屏蔽企业信息表 Mapper 接口
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-23
 */
public interface UserBlockedEnterpriseMapper extends BaseMapper<UserBlockedEnterprise> {

    /**
     * 查询个人屏蔽的企业
     *
     * @param userId
     * @param personalBlockedEnterpriseDto
     * @return
     */
    PersonalBlockedEnterpriseVo queryPersonalBlockedEnterprise(@Param("userId") Long userId, @Param("enterpriseId") Long enterpriseId, @Param("personalBlockedEnterpriseDto") QueryPersonalBlockedEnterpriseDto personalBlockedEnterpriseDto);

    /**
     * 根据企业名称查询企业
     *
     * @param queryEnterpriseByNameDto
     * @return
     */
    QueryEnterpriseByNameVo queryEnterpriseByName(@Param("queryEnterpriseByNameDto") QueryEnterpriseByNameDto queryEnterpriseByNameDto);
}
