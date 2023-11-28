package com.korant.youya.workplace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.enums.userenterprise.UserEnterpriseLimitStatus;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EnterpriseTodoMapper;
import com.korant.youya.workplace.mapper.RoleMapper;
import com.korant.youya.workplace.mapper.UserEnterpriseMapper;
import com.korant.youya.workplace.mapper.UserRoleMapper;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseQueryListDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseRemoveDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseTransferDto;
import com.korant.youya.workplace.pojo.po.EnterpriseTodo;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.pojo.po.UserRole;
import com.korant.youya.workplace.pojo.vo.userenterprise.UserEnterpriseColleagueInfoVo;
import com.korant.youya.workplace.service.UserEnterpriseService;
import com.korant.youya.workplace.utils.SpringSecurityUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private EnterpriseTodoMapper enterpriseTodoMapper;

    /**
     * @Description 解除关联企业绑定
     * @Param
     * @return
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbinding() {

        //判断当前用户是否为企业管理员
        Long userId = SpringSecurityUtil.getUserId();
        UserEnterprise userEnterprise = userEnterpriseMapper.selectOne(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0));
        if (userEnterprise == null) throw new YouyaException("您当前未绑定任何公司！");
        Long role = roleMapper.getRoleByUserAndEnterprise(userId, userEnterprise.getEnterpriseId());
        //TODO 角色后面修改
        if (role !=null && role == 2) throw new YouyaException("管理员无法自己解绑！");
        if (role !=null && role == 1) throw new YouyaException("HR请在企业端进行解绑！");
        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .set(UserEnterprise::getIsDelete, 1));

        //提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userId)
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

    /**
     * 退出公司
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exit(Long id) {

        Long userId = SpringSecurityUtil.getUserId();

        Long role = roleMapper.getRoleByUserAndEnterprise(userId, id);
        //TODO 管理员角色相关后续考虑
        if (role !=null && role == 2) throw new YouyaException("管理员无法退出公司！");

        //HR退出公司后 已发布职位发布人自动转为公司管理员
        if (role !=null && role == 1) {
            //解除hr角色关系
            userRoleMapper.update(null,
                    new LambdaUpdateWrapper<UserRole>()
                            .eq(UserRole::getUid, userId)
                            .set(UserRole::getIsDelete, 1));
        }

        //解除公司人员关系
        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .eq(UserEnterprise::getEnterpriseId, id)
                        .set(UserEnterprise::getIsDelete, 1));

        //提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userId)
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(UserEnterpriseRemoveDto userEnterpriseRemoveDto) {

        Long userId = SpringSecurityUtil.getUserId();

        //解除hr跟公司的绑定
        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userEnterpriseRemoveDto.getUid())
                        .eq(UserEnterprise::getEnterpriseId, userEnterpriseRemoveDto.getEnterpriseId())
                        .set(UserEnterprise::getIsDelete, 1));

        //TODO 删除当前用户的hr角色
        userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUid, userEnterpriseRemoveDto.getUid())
                        .set(UserRole::getIsDelete, 1));

        //提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userEnterpriseRemoveDto.getUid())
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

    /**
     * 根据姓名查询公司同事
     *
     * @param
     * @return
     */
    @Override
    public Page<UserEnterpriseColleagueInfoVo> queryColleagueByName(UserEnterpriseQueryListDto userEnterpriseQueryListDto) {

        Long userId = SpringSecurityUtil.getUserId();
        int pageNumber = userEnterpriseQueryListDto.getPageNumber();
        int pageSize = userEnterpriseQueryListDto.getPageSize();
        Integer count = userEnterpriseMapper.queryCountColleagueByName(userEnterpriseQueryListDto.getId(), userEnterpriseQueryListDto.getName(), userId);
        List<UserEnterpriseColleagueInfoVo> list = userEnterpriseMapper.queryColleagueByName(userEnterpriseQueryListDto.getId(), userEnterpriseQueryListDto.getName(), userId, pageNumber, pageSize);
        Page<UserEnterpriseColleagueInfoVo> page = new Page<>();
        if (count == null){
            count = 0;
        }
        page.setRecords(list).setCurrent(pageNumber).setSize(pageSize).setTotal(count);
        return page;

    }

    /**
     * 转让公司
     *
     * @param
     * @return
     */
    @Override
    public void transfer(UserEnterpriseTransferDto userEnterpriseQueryListDto) {

        Long userId = SpringSecurityUtil.getUserId();

        //删除角色关系 公司关系
        userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUid, userId)
                        .set(UserRole::getIsDelete, 1));

        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .set(UserEnterprise::getIsDelete, 1));

        //被转让者变成公司的管理员
        //先删除被转让者的角色
        userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUid, userEnterpriseQueryListDto.getUid())
                        .set(UserRole::getIsDelete, 1));

        //TODO 成为管理员角色
        UserRole userRole = new UserRole();
        userRole.setUid(userEnterpriseQueryListDto.getUid());
        userRole.setRid(2L);
        userRoleMapper.insert(userRole);

        //被转让者提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userEnterpriseQueryListDto.getUid())
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

    /**
     * 判断当前人员是否有关联公司
     *
     * @param
     * @return
     */
    @Override
    public Integer isLimit() {

        Long userId = SpringSecurityUtil.getUserId();

        boolean exists = userEnterpriseMapper.exists(new LambdaQueryWrapper<UserEnterprise>().eq(UserEnterprise::getUid, userId).eq(UserEnterprise::getIsDelete, 0));
        if (exists){
            return UserEnterpriseLimitStatus.IS_LIMIT.getStatus();
        }else {
            boolean todo = enterpriseTodoMapper.exists(new LambdaQueryWrapper<EnterpriseTodo>()
                    .eq(EnterpriseTodo::getUid, userId)
                    .eq(EnterpriseTodo::getOperate, 0)
                    .eq(EnterpriseTodo::getIsDelete, 0));
            if (todo){
                return UserEnterpriseLimitStatus.IN_PROGRESS.getStatus();
            }else {
                return UserEnterpriseLimitStatus.NOT_LIMIT.getStatus();
            }
        }
    }


}
