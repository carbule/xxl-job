package com.korant.youya.workplace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.korant.youya.workplace.pojo.po.UserEnterprise;

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
    void unbinding(Long id);

    /**
     * 退出公司
     *
     * @return
     */
    void exit(Long id);

}
