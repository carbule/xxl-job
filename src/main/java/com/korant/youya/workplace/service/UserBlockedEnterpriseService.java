package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.PageData;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryEnterpriseByNameDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.QueryPersonalBlockedEnterpriseDto;
import com.korant.youya.workplace.pojo.dto.userblockedenterprise.UserBlockedEnterpriseCreateDto;
import com.korant.youya.workplace.pojo.po.UserBlockedEnterprise;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.PersonalBlockedEnterpriseVo;
import com.korant.youya.workplace.pojo.vo.userblockedenterprise.QueryEnterpriseByNameVo;

/**
 * <p>
 * 用户屏蔽企业信息表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-23
 */
public interface UserBlockedEnterpriseService extends IService<UserBlockedEnterprise> {

    /**
     * 查询个人屏蔽的企业
     *
     * @param personalBlockedEnterpriseDto
     * @return
     */
    PageData<PersonalBlockedEnterpriseVo> queryPersonalBlockedEnterprise(QueryPersonalBlockedEnterpriseDto personalBlockedEnterpriseDto);

    /**
     * 根据企业名称查询企业
     *
     * @param queryEnterpriseByNameDto
     * @return
     */
    QueryEnterpriseByNameVo queryEnterpriseByName(QueryEnterpriseByNameDto queryEnterpriseByNameDto);

    /**
     * 屏蔽当前所在企业
     */
    void blockCurrentEnterprise();

    /**
     * 创建屏蔽企业
     *
     * @param userBlockedEnterpriseCreateDto
     */
    void create(UserBlockedEnterpriseCreateDto userBlockedEnterpriseCreateDto);

    /**
     * 删除屏蔽企业
     *
     * @param id
     */
    void delete(Long id);
}
