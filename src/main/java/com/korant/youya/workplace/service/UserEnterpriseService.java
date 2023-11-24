package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseQueryListDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseRemoveDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseTransferDto;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.pojo.vo.userenterprise.UserEnterpriseColleagueInfoVo;

/**
 * <p>
 * 用户企业关联表 服务类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
public interface UserEnterpriseService extends IService<UserEnterprise> {

    /**
     * 解除关联企业绑定
     *
     * @return
     */
    void unbinding();

    /**
     * 退出公司
     *
     * @return
     */
    void exit(Long id);

    /**
     * 管理员移除公司下用户(HR)
     *
     * @param
     * @return
     */
    void removeUser(UserEnterpriseRemoveDto userEnterpriseRemoveDto);

    /**
     * 根据姓名查询公司同事
     *
     * @param
     * @return
     */
    Page<UserEnterpriseColleagueInfoVo> queryColleagueByName(UserEnterpriseQueryListDto userEnterpriseQueryListDto);

    /**
     * 转让公司
     *
     * @param
     * @return
     */
    void transfer(UserEnterpriseTransferDto userEnterpriseQueryListDto);

    /**
     * 判断当前人员是否有关联公司
     *
     * @param
     * @return
     */
    Integer isLimit();
}
