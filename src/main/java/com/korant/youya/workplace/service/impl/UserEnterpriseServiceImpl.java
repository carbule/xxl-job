package com.korant.youya.workplace.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.role.RoleEnum;
import com.korant.youya.workplace.enums.userenterprise.UserEnterpriseLimitStatus;
import com.korant.youya.workplace.exception.YouyaException;
import com.korant.youya.workplace.mapper.EnterpriseTodoMapper;
import com.korant.youya.workplace.mapper.RoleMapper;
import com.korant.youya.workplace.mapper.UserEnterpriseMapper;
import com.korant.youya.workplace.mapper.UserRoleMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseQueryListDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseRemoveDto;
import com.korant.youya.workplace.pojo.dto.userenterprise.UserEnterpriseTransferDto;
import com.korant.youya.workplace.pojo.po.EnterpriseTodo;
import com.korant.youya.workplace.pojo.po.UserEnterprise;
import com.korant.youya.workplace.pojo.po.UserRole;
import com.korant.youya.workplace.pojo.vo.userenterprise.UserEnterpriseColleagueInfoVo;
import com.korant.youya.workplace.service.UserEnterpriseService;
import com.korant.youya.workplace.utils.RedisUtil;
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

    @Resource
    private RedisUtil redisUtil;

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

        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        LoginUser loginUser = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);
        loginUser.setEnterpriseId(null);
        //更新用户缓存
        redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));

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

        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        LoginUser loginUser = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);

        //TODO 退出公司后 已发布职位发布人自动转为公司管理员
        if (role !=null && role == 1) {
            //解除hr角色关系
            userRoleMapper.update(null,
                    new LambdaUpdateWrapper<UserRole>()
                            .eq(UserRole::getUid, userId)
                            .set(UserRole::getIsDelete, 1));
            loginUser.setRole(null);
        }

        //解除公司人员关系
        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userId)
                        .eq(UserEnterprise::getEnterpriseId, id)
                        .set(UserEnterprise::getIsDelete, 1));
        loginUser.setEnterpriseId(null);

        //更新用户缓存
        redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));

        //提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userId)
                        .set(EnterpriseTodo::getIsDelete, 1));

    }

    /**
     * 管理员移除公司下用户(HR)
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(UserEnterpriseRemoveDto userEnterpriseRemoveDto) {

        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userEnterpriseRemoveDto.getUid());
        LoginUser loginUser = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);
        if (loginUser.getRole() == null || !loginUser.getRole().equals(RoleEnum.ADMIN.getRole()))
            throw new YouyaException("您不是管理员，无权操作！");

        //解除hr跟公司的绑定
        userEnterpriseMapper.update(null,
                new LambdaUpdateWrapper<UserEnterprise>()
                        .eq(UserEnterprise::getUid, userEnterpriseRemoveDto.getUid())
                        .set(UserEnterprise::getIsDelete, 1));
        loginUser.setEnterpriseId(null);

        //TODO 删除当前用户的hr角色
        userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUid, userEnterpriseRemoveDto.getUid())
                        .set(UserRole::getIsDelete, 1));
        loginUser.setRole(null);
        //更新用户缓存
        redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));

        //提交过的申请删除
        enterpriseTodoMapper.update(null,
                new LambdaUpdateWrapper<EnterpriseTodo>()
                        .eq(EnterpriseTodo::getUid, userEnterpriseRemoveDto.getUid())
                        .set(EnterpriseTodo::getIsDelete, 1));

        //TODO hr当前已经发布的职位全部移交到管理员手中

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
    @Transactional(rollbackFor = Exception.class)
    public void transfer(UserEnterpriseTransferDto userEnterpriseQueryListDto) {

        Long userId = SpringSecurityUtil.getUserId();

        String cacheKey = String.format(RedisConstant.YY_USER_CACHE, userId);
        LoginUser loginUser = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);
        if (loginUser.getRole() == null || !loginUser.getRole().equals(RoleEnum.ADMIN.getRole()))
            throw new YouyaException("您不是管理员，无权操作！");
        //转让者角色变为hr
        userRoleMapper.update(null,
                new LambdaUpdateWrapper<UserRole>()
                        .eq(UserRole::getUid, userId)
                        .eq(UserRole::getIsDelete, 0)
                        .set(UserRole::getRid, 1));
        loginUser.setRole(RoleEnum.HR.getRole());
        //更新用户缓存
        redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser));

        String TransfereeCacheKey = String.format(RedisConstant.YY_USER_CACHE, userEnterpriseQueryListDto.getUid());
        LoginUser Transferee = JSONObject.parseObject(String.valueOf(redisUtil.get(cacheKey)), LoginUser.class);
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
        Transferee.setRole(RoleEnum.ADMIN.getRole());
        //更新被转让者用户缓存
        redisUtil.set(TransfereeCacheKey, JSONObject.toJSONString(Transferee));

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
