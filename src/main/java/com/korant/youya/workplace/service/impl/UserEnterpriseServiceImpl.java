package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.RoleMapper;
import com.korant.youya.workplace.mapper.UserEnterpriseMapper;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.service.UserEnterpriseService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import io.netty.handler.codec.AsciiHeadersEncoder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户企业关联表 服务实现类
 * </p>
 *
 * @author chenyiqiang
 * @since 2023-11-14
 */
@Service
public class UserEnterpriseServiceImpl extends ServiceImpl<UserEnterpriseMapper, UserEnterprise> implements UserEnterpriseService {

    @Resource
    private UserEnterpriseMapper userEnterpriseMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * @Description 解除关联企业绑定
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbinding(Long id) {

        //判断当前用户是否为企业管理员
        Long userId = SpringSecurityUtil.getUserId();
        boolean exists = userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getEnterpriseId, id).eq(UserEnterprise::getIsDelete, 0));
        if (exists) throw new YouyaException("您当前未绑定任何公司！");
        Long role = roleMapper.getRoleByUserAndEnterprise(userId, id);
        //TODO 角色后面修改
        if (role == 2) throw new YouyaException("管理员无法自己解绑！");
        userEnterpriseMapper.update(new UserEnterprise(),
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .eq(UserEnterprise::getEnterpriseId, id)
                        .set(UserEnterprise::getIsDelete, 1));
    }

    /**
     * 退出公司
     *
     * @return
     */
    @Override
    public void exit(Long id) {

        Long userId = SpringSecurityUtil.getUserId();

        //TODO 管理员角色相关后续考虑

        //退出公司后 已发布职位发布人自动转为公司管理员

        //解除公司人员关系
        userEnterpriseMapper.update(new UserEnterprise(),
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .eq(UserEnterprise::getEnterpriseId, id)
                        .set(UserEnterprise::getIsDelete, 1));

    }
}
